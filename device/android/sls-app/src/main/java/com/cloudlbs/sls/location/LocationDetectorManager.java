package com.cloudlbs.sls.location;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.cloudlbs.sls.event.ActivationListener;
import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.ReadingFailedEvent;
import com.cloudlbs.sls.event.ReadingFailedListener;
import com.cloudlbs.sls.event.ReadingScheduleChangeEvent;
import com.cloudlbs.sls.event.ReadingScheduleChangeListener;
import com.cloudlbs.sls.event.ShutdownEvent;
import com.cloudlbs.sls.event.StartupEvent;
import com.cloudlbs.sls.utils.Logger;

/**
 * Follows an observer pattern to watch for reading schedule changes or reading
 * failures and turns the location detector on and off as necessary.<br/>
 * <br/>
 * Although this class itself is a {@link LocationListener}, it does not process
 * any readings. It simply listens for whether readings are occurring at all in
 * order to determine if the GPS should timeout without a fix. <br/>
 * <br/>
 * It also holds a reference to the last {@link Location} received from the
 * location provider, and resubmits it to the processor if no reading has come
 * in before the next scheduled reading is due. This allows the reading request
 * to re-evaluate whether it will still accept the old reading based on its max
 * allowable GPS age.
 * 
 * @author Dan Mascenik
 * 
 */
public abstract class LocationDetectorManager implements
		ReadingScheduleChangeListener, ActivationListener, LocationListener,
		ReadingFailedListener {

	/**
	 * How long before a scheduled location reading to start up the GPS chip so
	 * it can get a better fix in time for the reading. This must always be less
	 * than {@link #GPS_MAX_IDLE_MILLIS}.
	 */
	public static final int GPS_WARMUP_MILLIS = 30000;

	/**
	 * The max idle time to allow before shutting off the GPS chip to save
	 * battery. This must always be greater than {@link #GPS_WARMUP_MILLIS}. If
	 * the next scheduled reading is more than this number of seconds away, the
	 * chip will be shut off for {@link #GPS_MAX_IDLE_MILLIS} -
	 * {@link #GPS_WARMUP_MILLIS} seconds (which must always be a positive
	 * number).
	 */
	public static final int GPS_MAX_IDLE_MILLIS = 45000;

	/**
	 * If the GPS fails to get a fix, how long to wait before trying again
	 * (5min).
	 */
	public static final int GPS_FAILED_WAIT_MILLIS = 300000;

	/**
	 * How long to wait for a fix before considering the GPS fix to have failed
	 * (30s);
	 */
	public static final int GPS_FAILED_TIMEOUT_MILLIS = 30000;

	/**
	 * The minimum amount of time from a schedule change until an existing
	 * reading will be resubmitted. This is to give the GPS a chance to provide
	 * a fresher reading.
	 */
	public static final int MIN_RESUBMIT_DELAY_MILLIS = 3000;

	private int gpsWarmupMillis = GPS_WARMUP_MILLIS;
	private int gpsMaxIdleMillis = GPS_MAX_IDLE_MILLIS;
	private int gpsFailedWaitMillis = GPS_FAILED_WAIT_MILLIS;

	protected long lastGpsReadingTimestamp;
	protected Location lastLocationReading;
	protected LocationProcessor processor;
	protected int satelliteCount;

	public LocationDetectorManager(LocationProcessor processor) {
		this.processor = processor;
		EventDispatcher.addListener(this);
	}

	protected LocationDetectorManager(LocationProcessor processor,
			int gpsWarmupMillis, int gpsMaxIdleMillis, int gpsFailedWaitMillis) {
		this(processor);
		this.gpsFailedWaitMillis = gpsFailedWaitMillis;
		this.gpsMaxIdleMillis = gpsMaxIdleMillis;
		this.gpsWarmupMillis = gpsWarmupMillis;
	}

	/**
	 * Starts up the location detector and the timer for determining if it
	 * failed to get a fix.
	 */
	private void activateLocationDetector() {
		doActivateLocationDetector();
		long now = System.currentTimeMillis();
		this.lastGpsReadingTimestamp = now;
		cancelFutureTask(dispatchReadingFailed);
		scheduleTaskAfterDelay(dispatchReadingFailed, GPS_FAILED_TIMEOUT_MILLIS);
	}

	/**
	 * Stops the location detector clears the timeout timer.
	 */
	private void deactivateLocationDetector() {
		doDeactivateLocationDetector();
		cancelFutureTask(dispatchReadingFailed);
		lastGpsReadingTimestamp = 0;
	}

	/**
	 * Check the schedule to see if the GPS needs to be on or off, and possibly
	 * set a timer for when this class should reevaluate.
	 */
	@Override
	public void onReadingScheduleChange(ReadingScheduleChangeEvent evt) {
		final ReadingSchedule schedule = evt.getSchedule();
		long now = System.currentTimeMillis();
		long next = 0;
		if (schedule.size() > 0) {
			next = schedule.peek().getReadingTime();
		}

		/*
		 * Set a timer to resubmit the last known location reading in case no
		 * more readings come in before the next one is due. It's possible that
		 * the next reading may allow an old enough GPS age that the last
		 * reading is still valid.
		 */
		cancelFutureTask(resubmitLocation);
		if (next != 0) {

			/*
			 * Note that this means the location processor may get two of the
			 * same reading in rapid succession. The first one accepted will
			 * fulfill the reading request.
			 */
			long resubmitDelay = next - now;
			if (resubmitDelay < MIN_RESUBMIT_DELAY_MILLIS) {

				// Give the GPS a chance to warm up and get a fresh reading
				resubmitDelay = MIN_RESUBMIT_DELAY_MILLIS;
			}
			scheduleTaskAfterDelay(resubmitLocation, resubmitDelay);
		}

		/*
		 * Detector on/off logic
		 */
		if (isLocationDetectorActive()) {
			/*
			 * Should the detector be turned off immediately?
			 */
			if (schedule.size() == 0 || next > now + gpsMaxIdleMillis) {

				/*
				 * No readings within the timeout window
				 */
				deactivateLocationDetector();

				scheduleWakeupCall(schedule);

			}
		} else {
			/*
			 * Should the detector be turned on immediately?
			 */
			if (schedule.size() > 0 && next < now + gpsWarmupMillis) {

				/*
				 * Upcoming reading within the warmup window
				 */
				activateLocationDetector();

			} else {

				scheduleWakeupCall(schedule);

			}
		}
	}

	/**
	 * Shutdown the location detector and set a timer to try again in 5 minutes
	 * unless another schedule change comes in.
	 */
	@Override
	public void onReadingFailed(ReadingFailedEvent evt) {
		if (isLocationDetectorActive()) {
			if (satelliteCount < 4) {
				Logger.debug("GPS reading timed out");
				deactivateLocationDetector();
				cancelFutureTask(activationWakeupCall);
				scheduleTaskAfterDelay(activationWakeupCall,
						gpsFailedWaitMillis);
			} else {
				/*
				 * The device just isn't moving
				 */
				cancelFutureTask(dispatchReadingFailed);
				scheduleTaskAfterDelay(dispatchReadingFailed,
						GPS_FAILED_TIMEOUT_MILLIS);
			}
		}
	}

	/**
	 * Schedule a wakeup notification for the warmup time of the next scheduled
	 * reading.
	 * 
	 * @param schedule
	 */
	private void scheduleWakeupCall(ReadingSchedule schedule) {
		if (schedule.size() > 0) {
			ReadingRequest next = schedule.peek();
			long wakeupDelay = System.currentTimeMillis()
					- next.getReadingTime() - gpsWarmupMillis;
			if (wakeupDelay < 1) {
				wakeupDelay = 1;
			}
			cancelFutureTask(scheduleWakeupCall);
			scheduleWakeupCall.schedule = schedule;
			scheduleTaskAfterDelay(scheduleWakeupCall, wakeupDelay);
		}
	}

	protected abstract void scheduleTaskAfterDelay(Runnable r, long delay);

	protected abstract void cancelFutureTask(Runnable r);

	private Runnable activationWakeupCall = new Runnable() {

		@Override
		public void run() {
			activateLocationDetector();
		}
	};

	private ScheduleRunnable scheduleWakeupCall = new ScheduleRunnable();

	private Runnable dispatchReadingFailed = new Runnable() {

		@Override
		public void run() {
			EventDispatcher.dispatchEvent(new ReadingFailedEvent());
		}
	};

	private Runnable resubmitLocation = new Runnable() {

		@Override
		public void run() {
			if (lastLocationReading != null) {
				processor.onLocationChanged(lastLocationReading);
			}
		}
	};

	@Override
	public synchronized void onStartupEvent(StartupEvent evt) {
		// Does nothing
	}

	@Override
	public synchronized void onShutdown(ShutdownEvent evt) {
		deactivateLocationDetector();
	}

	protected abstract void doDeactivateLocationDetector();

	protected abstract void doActivateLocationDetector();

	protected abstract boolean isLocationDetectorActive();

	/**
	 * Update the {@link #lastLocationReading} and
	 * {@link #lastGpsReadingTimestamp}, and reset the timeout timer.
	 */
	@Override
	public void onLocationChanged(Location loc) {
		Logger.debug("Got a reading, reseting timout clock");

		long now = System.currentTimeMillis();
		this.lastGpsReadingTimestamp = now;
		this.lastLocationReading = loc;

		cancelFutureTask(dispatchReadingFailed);
		scheduleTaskAfterDelay(dispatchReadingFailed, GPS_FAILED_TIMEOUT_MILLIS);
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// Does nothing
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// Does nothing
	}

	/**
	 * Captures the satellite count. If the satellite count is greater than 3,
	 * then a fix is possible. This is used to determine whether a reading has
	 * not been registered because of lack of satellite coverage or because the
	 * device is simply not moving.
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		if (extras != null && provider.equals(LocationManager.GPS_PROVIDER)) {
			satelliteCount = extras.getInt("satellites");
		}
	}

	class ScheduleRunnable implements Runnable {
		ReadingSchedule schedule;

		@Override
		public void run() {
			EventDispatcher.dispatchEvent(new ReadingScheduleChangeEvent(
					schedule));
		}

	}

}

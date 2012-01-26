package com.cloudlbs.sls.location;

import android.location.LocationManager;
import android.os.Handler;

import com.cloudlbs.sls.utils.Assert;
import com.cloudlbs.sls.utils.Logger;

/**
 * 
 * @author Dan Mascenik
 * 
 */
public class AndroidLocationDetectorManager extends LocationDetectorManager {

	/**
	 * This lock is held while the GPS is being activated or deactivated
	 */
	private Object gpsActivationLock = new Object();

	// Flags whether GPS chip is on or off
	private boolean isGpsActive = false;

	/*
	 * Get device updates at the max frequency allowed by the schedule (1s
	 * granularity)
	 */
	private static final long UPDATE_FREQ_MILLIS = 1000;

	private static final float MIN_DISTANCE_METERS = 0.5f;

	private LocationManager locationManager;

	/**
	 * @param processor
	 * @param locationManager
	 */
	public AndroidLocationDetectorManager(LocationProcessor processor,
			LocationManager locationManager) {
		super(processor);
		Assert.notNull(processor);
		Assert.notNull(locationManager);
		this.processor = processor;
		this.locationManager = locationManager;
	}

	@Override
	protected boolean isLocationDetectorActive() {
		synchronized (gpsActivationLock) {
			return isGpsActive;
		}
	}

	@Override
	protected void doActivateLocationDetector() {
		mainHandler.post(gpsActivator);
	}

	@Override
	protected void doDeactivateLocationDetector() {
		mainHandler.post(gpsDeactivator);
	}

	@Override
	protected void scheduleTaskAfterDelay(Runnable r, long delay) {
		mainHandler.postDelayed(r, delay);
	}

	@Override
	protected void cancelFutureTask(Runnable r) {
		mainHandler.removeCallbacks(r);
	}

	/**
	 * Anything interacting with the native android services has to be done on
	 * the main thread using the {@link Runnable}s below.
	 */
	private final Handler mainHandler = new Handler();

	private final Runnable gpsActivator = new Runnable() {

		@Override
		public void run() {
			synchronized (gpsActivationLock) {
				if (isGpsActive == false) {
					Logger.debug("Starting up GPS for next reading");
					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, UPDATE_FREQ_MILLIS,
							MIN_DISTANCE_METERS, processor);
					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, UPDATE_FREQ_MILLIS,
							MIN_DISTANCE_METERS,
							AndroidLocationDetectorManager.this);
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							UPDATE_FREQ_MILLIS, MIN_DISTANCE_METERS, processor);
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							UPDATE_FREQ_MILLIS, MIN_DISTANCE_METERS,
							AndroidLocationDetectorManager.this);
					isGpsActive = true;
				}
			}
		}
	};

	private final Runnable gpsDeactivator = new Runnable() {

		@Override
		public void run() {
			synchronized (gpsActivationLock) {
				if (isGpsActive == true) {
					Logger.debug("Turning off GPS to save battery");
					locationManager.removeUpdates(processor);
					locationManager
							.removeUpdates(AndroidLocationDetectorManager.this);
					isGpsActive = false;
				}
			}
		}
	};

}

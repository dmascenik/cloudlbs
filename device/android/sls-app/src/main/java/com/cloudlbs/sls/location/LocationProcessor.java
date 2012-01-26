package com.cloudlbs.sls.location;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.utils.Assert;
import com.cloudlbs.sls.utils.Logger;

/**
 * Listens for location readings and checks the {@link ReadingSchedule} for any
 * {@link ReadingRequest}s that are fulfilled by the reading. If the reading is
 * satisfactory, a {@link LocationReadingEvent} is dispatched and the request is
 * cleared from the schedule.
 * 
 * @author Dan Mascenik
 * 
 */
public class LocationProcessor implements LocationListener {

	private ReadingSchedule schedule;

	public LocationProcessor(ReadingSchedule schedule) {
		Assert.notNull(schedule);
		this.schedule = schedule;
	}

	@Override
	public void onLocationChanged(Location location) {
		long now = System.currentTimeMillis();
		long age = now - location.getTime();
		Logger.debug("Received reading " + location.getLatitude() + "/"
				+ location.getLongitude() + "(" + age + "ms)");

		/*
		 * Must lock the schedule while this is going on
		 */
		boolean hasChanges = false;
		ArrayList<ReadingRequest> recurrences = new ArrayList<ReadingRequest>();
		synchronized (schedule) {
			ReadingRequest[] requests = schedule.toArray(new ReadingRequest[0]);
			for (int i = 0; i < requests.length; i++) {
				ReadingRequest req = requests[i];
				String name = req.getApiKey();
				if (req.hasExpired()) {
					hasChanges = true;
					schedule.deferNotifications();
					Logger.warn("Removing expired reading request from " + name);
					schedule.remove(req);
					scheduleRepeatReading(req, recurrences);
				}

				/*
				 * Emulator returns location times that are before the current
				 * system time. This doesn't happen on real devices. This
				 * assumes that the max wait for a reading cannot be more than
				 * 2m, and that the reading expiration is 15m. Any requests that
				 * allow a reading older than that will already have expired
				 * from the system, so this code doesn't have to worry about
				 * resubmits as long as time is only tweaked for readings with
				 * times more than 17m old. Using 30m to be conservative here.
				 */
				long readingTime = location.getTime();
				if (readingTime < now - 1800000) {
					readingTime = now;
					Logger.warn("Emulator problem - had to tweak location time to current");
				}

				if (req.accepts(readingTime, location.getAccuracy())) {
					hasChanges = true;
					schedule.deferNotifications();
					Logger.debug("Location accepted by request from " + name);
					LocationReading lr;
					lr = new LocationReading(req.getApiKey(),
							location.getLatitude(), location.getLongitude(),
							location.getAltitude(), location.getAccuracy(),
							location.getTime());
					lr.setFixTime((int) (readingTime - req.getReadingTime()));
					lr.setTimedOut((now - req.getReadingTime()) > req
							.getParams().getMaxWaitSeconds() * 1000);
					EventDispatcher.dispatchEvent(lr);
					schedule.remove(req);
					scheduleRepeatReading(req, recurrences);
				}
			}
		}

		if (hasChanges) {
			for (ReadingRequest req : recurrences) {
				EventDispatcher.dispatchEvent(req);
			}

			Logger.debug("Reading processed successfully. Notifying of schedule changes");
			schedule.forceNotifyListeners();
		}
	}

	void scheduleRepeatReading(ReadingRequest req,
			List<ReadingRequest> recurrences) {
		long now = System.currentTimeMillis();
		if (req.isRecurring()
				&& (req.getRepeatUntil() == 0 || req.getRepeatUntil() > now)) {
			Logger.debug("Scheduling next recurring request");
			ReadingRequest next = req.clone();
			next.setReadingTime(now + req.getParams().getFrequencySeconds()
					* 1000);
			recurrences.add(next);
		} else {
			Logger.debug("Not rescheduling non-recurring request");
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		Logger.debug("Provider disabled: " + provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Logger.debug("Provider enabled: " + provider);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		String s;
		switch (status) {
		case LocationProvider.OUT_OF_SERVICE:
			s = "Out of Service";
			break;

		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			s = "Temporarily Unavailable";
			break;

		case LocationProvider.AVAILABLE:
			s = "Available";
			break;

		default:
			s = "Unknown Status";
			break;
		}

		String satellites = "";
		if (extras != null && provider.equals(LocationManager.GPS_PROVIDER)) {
			int sats = extras.getInt("satellites");
			satellites = " (" + sats + " satellites)";
		}

		Logger.debug(provider + " status change: " + s + satellites);
	}
}

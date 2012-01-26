package com.cloudlbs.sls.location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.ReadingRequestListener;

/**
 * Manages the schedule of location readings to be taken. This is the only class
 * that can add or remove {@link ReadingRequest}s to or from the schedule. The
 * {@link LocationProcessor} is the only other class that can modify the
 * schedule, and only by removing expired or completed {@link ReadingRequest}s.<br/>
 * <br/>
 * A reading may be scheduled by dispatching a {@link ReadingRequest} to the
 * {@link EventDispatcher}, or canceled by dispatching a
 * {@link CancelReadingRequest}.<br/>
 * <br/>
 * The {@link ScheduleManager} provides a user-facing means of modifying the
 * schedule, but is unaware of any classes that observe or act upon the
 * schedule. <br/>
 * <br/>
 * Restarting the service clears the schedule.
 * 
 * @author Dan Mascenik
 * 
 */
public class ScheduleManager implements ReadingRequestListener {

	/**
	 * Contains the schedule of location readings to be taken
	 */
	private ReadingSchedule schedule;

	public ScheduleManager() {
		schedule = new ReadingSchedule();
		EventDispatcher.addListener(this);
	}

	@Override
	public void onReadingRequest(ReadingRequest request) {
		scheduleReading(request);
	}

	@Override
	public void onCancelReadingRequest(CancelReadingRequest cancelation) {
		cancelReading(cancelation);
	}

	/**
	 * Schedules a location reading. The schedule is locked during this
	 * operation.
	 */
	private void scheduleReading(ReadingRequest request) {
		synchronized (schedule) {
			schedule.add(request);
		}
	}

	/**
	 * Cancels all reading requests for an app or a remote session. The schedule
	 * is locked during this operation.
	 */
	private void cancelReading(CancelReadingRequest cancelation) {
		synchronized (schedule) {
			schedule.deferNotifications();
			ReadingRequest[] requests = schedule.toArray(new ReadingRequest[0]);
			for (int i = 0; i < requests.length; i++) {
				ReadingRequest req = requests[i];
				if (cancelation.getApiKey().equals(req.getApiKey())) {
					schedule.remove(req);
				}
			}
			schedule.forceNotifyListeners();
		}
	}

	/**
	 * Returns a {@link Collection} of {@link ReadingRequest} clones that match
	 * the provided API key.
	 * 
	 * @param apiKey
	 */
	public Collection<ReadingRequest> getReadingRequests(String apiKey) {
		ArrayList<ReadingRequest> requests = new ArrayList<ReadingRequest>();
		synchronized (schedule) {
			Iterator<ReadingRequest> iter = schedule.iterator();
			while (iter.hasNext()) {
				ReadingRequest req = iter.next();
				if (apiKey.equals(req.getApiKey())) {
					requests.add(req.clone());
				}
			}
		}
		return requests;
	}

	/**
	 * Returns a clone of the next {@link ReadingRequest} that matches the
	 * provided API key
	 * 
	 * @param apiKey
	 */
	public ReadingRequest getNextReadingRequests(String apiKey) {
		ReadingRequest request = null;
		synchronized (schedule) {
			Iterator<ReadingRequest> iter = schedule.iterator();
			while (iter.hasNext()) {
				ReadingRequest req = iter.next();
				if (apiKey.equals(req.getApiKey())) {
					request = req.clone();
					break;
				}
			}
		}
		return request;
	}

	public ReadingSchedule getSchedule() {
		return this.schedule;
	}

}

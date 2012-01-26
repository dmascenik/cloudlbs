package com.cloudlbs.sls.location;

import java.util.Collection;
import java.util.Iterator;
import java.util.PriorityQueue;

import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.ReadingScheduleChangeEvent;
import com.cloudlbs.sls.utils.Logger;

/**
 * This is essentially a wrapper for a {@link PriorityQueue} containing the
 * {@link ReadingRequest}s. Whenever the queue is modified, a
 * {@link ReadingScheduleChangeEvent} is fired. If a lot of changes are expected
 * to happen in rapid succession, call {@link #deferNotifications()} to avoid
 * firing events for each change. Then call {@link #forceNotifyListeners()} to
 * cancel the notification deferal.
 * 
 * 
 * @author Dan Mascenik
 * 
 */
public class ReadingSchedule extends PriorityQueue<ReadingRequest> {

	private static final long serialVersionUID = 5468437957764448791L;

	private boolean isNotificationDefered = false;

	@Override
	public boolean add(ReadingRequest e) {
		boolean result = super.add(e);
		notifyListeners();
		return result;
	}

	@Override
	public void clear() {
		super.clear();
		notifyListeners();
	}

	@Override
	public ReadingRequest poll() {
		ReadingRequest result = super.poll();
		notifyListeners();
		return result;
	}

	@Override
	public boolean remove(Object o) {
		boolean result = super.remove(o);
		notifyListeners();
		return result;
	}

	@Override
	public boolean addAll(Collection<? extends ReadingRequest> arg0) {
		boolean result = super.addAll(arg0);
		notifyListeners();
		return result;
	}

	@Override
	public ReadingRequest remove() {
		ReadingRequest result = super.remove();
		notifyListeners();
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		boolean result = super.removeAll(arg0);
		notifyListeners();
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		boolean result = super.retainAll(arg0);
		notifyListeners();
		return result;
	}

	public void deferNotifications() {
		this.isNotificationDefered = true;
	}

	public void forceNotifyListeners() {
		this.isNotificationDefered = false;
		notifyListeners();
	}

	private void notifyListeners() {
		if (!isNotificationDefered) {
			EventDispatcher.dispatchEvent(new ReadingScheduleChangeEvent(this));
			Logger.debug(toString());
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Reading schedule:\n");
		if (size() > 0) {
			Iterator<ReadingRequest> iter = iterator();
			while (iter.hasNext()) {
				ReadingRequest req = iter.next();
				String isRec = "n";
				if (req.isRecurring()) {
					isRec = "r";
				}
				String name = req.getApiKey();
				sb.append("   " + isRec + " " + req.getReadingTime() + " "
						+ name);
				if (iter.hasNext()) {
					sb.append("\n");
				}
			}
		} else {
			sb.append("   [empty]");
		}
		return sb.toString();
	}
}

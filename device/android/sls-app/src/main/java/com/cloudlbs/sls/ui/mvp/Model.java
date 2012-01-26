package com.cloudlbs.sls.ui.mvp;

import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.ModelEvent;
import com.cloudlbs.sls.utils.Logger;

/**
 * This class dispatches {@link ModelEvent}s when a field changes, so the
 * {@link EventDispatcher} must be initialized before it will work.
 * 
 * @author Dan Mascenik
 * 
 */
public abstract class Model {

	private boolean deferNotifications;
	private boolean hasChanges = true;
	private boolean noLogging;

	/**
	 * If notifications are not being deferred, dispatches a {@link ModelEvent}
	 * and unsets the model dirty flag.
	 */
	public void notifyViews() {
		notifyViews(false);
	}

	/**
	 * Cancels any notification deferal and notifies views of model changes.
	 */
	public void forceNotifyViews() {
		notifyViews(true);
	}

	/**
	 * Cancels any notification deferral and notifies the views of any model
	 * changes.
	 * 
	 * @see #deferNotifications()
	 * 
	 * @param cancelDeferral
	 */
	public void notifyViews(boolean cancelDeferral) {
		if (deferNotifications && !cancelDeferral) {
			// do nothing
			return;
		}

		if (hasChanges) {
			if (!noLogging) {
				Logger.debug(Logger.logTag + "-ui",
						"Dispatching a ModelEvent for " + this);
			}
			EventDispatcher.dispatchEvent(new ModelEvent(this, noLogging));
			deferNotifications = false;
			hasChanges = false;
		}
	}

	/**
	 * Sometimes you don't want {@link ModelEvent}s to log. For example, when
	 * the {@link ModelEvent} is for updating a log buffer, logging the event
	 * would cause an infinite loop.
	 * 
	 * @param noLogging
	 */
	public void setNoLogging(boolean noLogging) {
		this.noLogging = noLogging;
	}

	/**
	 * If a bunch of model changes are going to be made, this flag should be set
	 * to avoid repeated notifications on each individual change. Notification
	 * deferral remains in effect until {@link #notifyViews(boolean)} is called
	 * with the argument <code>true</code>
	 * 
	 * @see #notifyViews(boolean)
	 * 
	 */
	public void deferNotifications() {
		this.deferNotifications = true;
	}

	/**
	 * Marks this model dirty so that {@link #notifyViews()} will have an
	 * effect.
	 * 
	 * @param hasChanges
	 */
	protected void setHasChanges(boolean hasChanges) {
		this.hasChanges = hasChanges;
	}

}

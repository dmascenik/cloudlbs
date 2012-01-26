package com.cloudlbs.sls.ui;

import com.cloudlbs.sls.ui.mvp.Model;
import com.cloudlbs.sls.utils.Logger;

/**
 * @author Dan Mascenik
 * 
 */
public class SLSStateModel extends Model {

	private boolean isEnabled = false;
	private boolean isConnected = false;
	private boolean isStarting = false;
	private boolean isStopping = false;
	private boolean isPinging = false;
	private int status = 0;

	/**
	 * Is the SLS service enabled? This information comes from the SLS service
	 * itself, so the service must be connected (bound) for this to be known.
	 */
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
		setHasChanges(true);
		notifyViews();
	}

	/**
	 * Are we connected to the SLS service? This does not indicate that the
	 * service itself is running.
	 */
	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
		setHasChanges(true);
		Logger.debug(Logger.logTag + "-ui", "Calling notifyViews()");
		notifyViews();
	}

	public boolean isStarting() {
		return isStarting;
	}

	public synchronized void setStarting(boolean isStarting) {
		if (!isStopping && this.isStarting != isStarting) {
			this.isStarting = isStarting;
			setHasChanges(true);
			notifyViews();
		}
	}

	public boolean isStopping() {
		return isStopping;
	}

	public synchronized void setStopping(boolean isStopping) {
		this.isStopping = isStopping;
		setHasChanges(true);
		notifyViews();
	}

	public boolean isPinging() {
		return isPinging;
	}

	public void setPinging(boolean isPinging) {
		this.isPinging = isPinging;
		setHasChanges(true);
		notifyViews();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
		setHasChanges(true);
		notifyViews();
	}

}

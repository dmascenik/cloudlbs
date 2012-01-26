package com.cloudlbs.sls.ui;

import android.os.AsyncTask;

import com.cloudlbs.sls.SLSService;
import com.cloudlbs.sls.utils.Logger;

/**
 * Asynchronous task to start up or shut down the SLS. If one operation is
 * already in progress, a second is dropped.
 * 
 * @author Dan Mascenik
 * 
 */
public class StartStopTask extends AsyncTask<Void, Void, Boolean> {

	private SLSStateModel slsStateModel;
	private SLSService slsService;
	private boolean isStartup;

	private static boolean isInProgress;
	private boolean hasLock;

	public StartStopTask(SLSStateModel slsStateModel, SLSService slsService,
			boolean isStartup) {
		super();
		this.slsStateModel = slsStateModel;
		this.slsService = slsService;
		this.isStartup = isStartup;
	}

	@Override
	protected void onPreExecute() {
		getLock();
		if (isStartup) {
			Logger.debug("Startup completed onPreExecute(...)");
		} else {
			Logger.debug("Shutdown completed onPreExecute(...)");
		}
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		if (isStartup) {
			Logger.debug("Startup entered doInBackground(...)");
		} else {
			Logger.debug("Shutdown entered doInBackground(...)");
		}
		if (!hasLock) {
			throw new IllegalStateException("Task does not have lock!");
		}
		if (isStartup) {
			slsService.setEnabled(true);
		} else {
			slsService.setEnabled(false);
		}
		if (isStartup) {
			Logger.debug("Startup completed doInBackground(...)");
		} else {
			Logger.debug("Shutdown completed doInBackground(...)");
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (isStartup) {
			Logger.debug("Startup entered onPostExecute(...)");
		} else {
			Logger.debug("Shutdown entered onPostExecute(...)");
		}

		if (!hasLock) {
			throw new IllegalStateException("Task does not have lock!");
		}
		slsStateModel.setEnabled(slsService.isEnabled());
		if (isStartup) {
			slsStateModel.setStarting(false);
			slsStateModel.setStatus(slsService.getStatus().getFaultCode());
		} else {
			slsStateModel.setStopping(false);
		}
		Logger.debug("Releasing lock");
		releaseLock();
	}

	private synchronized void getLock() {
		try {
			while (isInProgress) {
				Logger.debug("Waiting for lock");
				wait();
			}
			Logger.debug("Lock available");
		} catch (InterruptedException e) {
			return;
		}

		if (isStartup) {
			Logger.debug("Startup lock aquired");
		} else {
			Logger.debug("Shutdown lock aquired");
		}

		// This thread's turn
		isInProgress = true;
		hasLock = true;
	}

	private synchronized void releaseLock() {
		isInProgress = false;
		hasLock = false;
		notify();

		if (isStartup) {
			Logger.debug("Startup lock released");
		} else {
			Logger.debug("Shutdown lock released");
		}

	}

}

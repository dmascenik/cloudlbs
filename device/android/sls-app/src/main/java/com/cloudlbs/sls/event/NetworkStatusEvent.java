package com.cloudlbs.sls.event;

import android.content.BroadcastReceiver;

/**
 * This type of event may be sent from a {@link BroadcastReceiver} listening for
 * network connectivity changes from the OS to any listener that may be
 * interested.
 * 
 * @author Dan Mascenik
 * 
 */
public class NetworkStatusEvent implements SLSEvent {

	private boolean isNetworkAvailable;

	public NetworkStatusEvent(boolean isNetworkAvailable) {
		this.isNetworkAvailable = isNetworkAvailable;
	}

	public boolean isNetworkAvailable() {
		return this.isNetworkAvailable;
	}

}

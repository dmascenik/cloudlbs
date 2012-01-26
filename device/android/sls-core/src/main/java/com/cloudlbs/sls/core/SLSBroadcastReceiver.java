package com.cloudlbs.sls.core;

import java.util.HashSet;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * This broadcast receiver must be registered by apps that will receive
 * callbacks from the SLS.
 * 
 * @author Dan Mascenik
 * 
 */
public class SLSBroadcastReceiver extends BroadcastReceiver {

	private Set<SLSClient> listeners = new HashSet<SLSClient>();

	@Override
	public void onReceive(Context context, Intent intent) {
		String packageName = intent
				.getStringExtra(SLSServiceConnection.APP_IDENTIFIER);
		if (context.getPackageName().equals(packageName)) {
			try {
				Log.d(SLSServiceConnection.CLIENT_LOG_TAG,
						"Received location data broadcast for " + packageName);
			} catch (Exception e) {
			}

			/*
			 * Broadcast is for this app, so notify all the listeners
			 */
			for (SLSClient listener : listeners) {
				listener.onSLSBroadcast();
			}

		}
	}

	public void addListener(SLSClient listener) {
		listeners.add(listener);
	}

	public void removeListener(SLSClient listener) {
		listeners.remove(listener);
	}

}

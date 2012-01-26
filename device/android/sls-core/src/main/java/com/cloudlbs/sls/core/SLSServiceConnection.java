package com.cloudlbs.sls.core;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * 
 * 
 * @author Dan Mascenik
 * 
 */
public class SLSServiceConnection implements ServiceConnection {

	/*
	 * These are stored in the client app's memory so they can be reused until
	 * either the credentials expire with the SLS or the app is terminated by
	 * the system.
	 */
	private static String username;
	private static String password;

	/**
	 * String name of the action broadcast by the SLS to notify of incoming
	 * location data
	 */
	public static final String NOTIFY_APPS_ACTION = "com.cloudlbs.sls.LOCATION_DATA_ACTION";
	private SLSBroadcastReceiver slsBroadcastReceiver = new SLSBroadcastReceiver();

	/**
	 * The string name of the "extra" field on the SLS location broadcast
	 * containing the app identifier (on android, the package name)
	 */
	public static final String APP_IDENTIFIER = "appID";

	/**
	 * The action to use when binding to the secure location service
	 */
	public static final String REMOTE_SERVICE_ACTION = "com.cloudlbs.sls.REMOTE_SERVICE";

	public static final String CLIENT_LOG_TAG = "sls-client";

	private SecureLocationService sls;
	private List<SLSClient> clients;
	private boolean serviceConnected;
	private String apiKey;
	private Context context;

	public SLSServiceConnection(Context context, String apiKey, SLSClient client) {
		this(context, apiKey);
		clients = new ArrayList<SLSClient>();
		clients.add(client);
	}

	public SLSServiceConnection(Context context, String apiKey,
			List<SLSClient> clients) {
		this(context, apiKey);
		this.clients = clients;
	}

	private SLSServiceConnection(Context context, String apiKey) {
		this.context = context;
		this.apiKey = apiKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.content.ServiceConnection#onServiceConnected(android.content.
	 * ComponentName, android.os.IBinder)
	 */
	@Override
	public synchronized void onServiceConnected(ComponentName name,
			IBinder service) {

		serviceConnected = false;
		ISecureLocationService isls = ISecureLocationService.Stub
				.asInterface(service);
		try {
			if (!isls.isEnabled()) {
				logError("SLS is disabled! Not notifying client classes");
				return;
			}
		} catch (RemoteException e) {
			logError("SLS isEnabled() call failed! "
					+ "Not notifying client classes");
			return;
		}

		sls = new SecureLocationService(isls, apiKey, username, password);

		for (SLSClient client : clients) {
			slsBroadcastReceiver.addListener(client);
			client.onSLSServiceConnected(sls);
		}

		context.registerReceiver(slsBroadcastReceiver, new IntentFilter(
				NOTIFY_APPS_ACTION));

		serviceConnected = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.content.ServiceConnection#onServiceDisconnected(android.content
	 * .ComponentName)
	 */
	@Override
	public synchronized void onServiceDisconnected(ComponentName name) {
		if (serviceConnected) {
			serviceConnected = false;
			context.unregisterReceiver(slsBroadcastReceiver);
			sls = null;
			for (SLSClient client : clients) {
				slsBroadcastReceiver.removeListener(client);
				client.onSLSServiceDisconnected();
			}
		}
	}

	public void unregisterReceiver() {
		context.unregisterReceiver(slsBroadcastReceiver);
	}
	
	private void logError(String msg) {
		try {
			Log.e(CLIENT_LOG_TAG, msg);
		} catch (Exception e) {
			// nevermind
		}
	}

	public boolean isServiceConnected() {
		return serviceConnected;
	}

	public SecureLocationService getSecureLocationService() {
		return sls;
	}

	public static void setUsername(String username) {
		SLSServiceConnection.username = username;
	}

	public static void setPassword(String password) {
		SLSServiceConnection.password = password;
	}
}

package com.cloudlbs.sls.core;

import java.util.List;

import android.os.RemoteException;
import android.util.Log;

/**
 * Provides a convenient wrapper for the {@link ISecureLocationService},
 * encapsulating all the repetitive validation of service availability,
 * authentication, and API key transmission.
 * 
 * @author Dan Mascenik
 * 
 */
public class SecureLocationService {

	private ISecureLocationService service;
	private String apiKey;
	private String username;
	private String password;

	public SecureLocationService(ISecureLocationService service, String apiKey,
			String username, String password) {
		this.service = service;
		this.apiKey = apiKey;
		this.username = username;
		this.password = password;
	}

	/**
	 * Checks if the current app is authenticated with the SLS using whatever
	 * credentials are currently in memory.
	 * 
	 * @see SLSServiceConnection#setUsername(String)
	 * @see SLSServiceConnection#setPassword(String)
	 * 
	 * @see ISecureLocationService#isAuthenticated(String, String)
	 */
	public boolean isAuthenticated() {
		boolean authenticated = false;
		if (username == null || username.trim().length() == 0
				|| password == null || password.trim().length() == 0) {
			return false;
		}

		try {
			authenticated = service.isAuthenticated(apiKey, username);
		} catch (RemoteException e) {
			logError("Error calling SLS");
		}

		if (authenticated) {
			logDebug("Still authenticated...");
		} else {
			logDebug("Credentials expired - try saved credentials");
			try {
				AuthenticationStatus auth = service.authenticate(apiKey,
						username, password);
				authenticated = auth.getIsSuccessful();
			} catch (RemoteException e) {
				logError("Error calling remote service");
			}

			if (authenticated) {
				logDebug("Reauthentication successful");
			} else {
				logDebug("Reauthentication unsuccessful");
			}
		}
		return authenticated;
	}

	/**
	 * @see ISecureLocationService#authenticate(String, String, String)
	 */
	public AuthenticationStatus authenticate(String username, String password) {
		AuthenticationStatus auth;
		try {
			auth = service.authenticate(apiKey, username, password);
		} catch (RemoteException e) {
			logError("Error calling SLS");
			auth = new AuthenticationStatus();
			auth.setIsSuccessful(false);
			auth.setFaultCode(AuthenticationStatus.FAULT_SERVICE_FAILURE);
		}
		return auth;
	}

	/**
	 * @see ISecureLocationService#logout(String)
	 */
	public void logout() {
		try {
			service.logout(apiKey);
		} catch (RemoteException e) {
			logError("Error calling SLS");
		}
	}

	/**
	 * @see ISecureLocationService#getLocation(String, LocationRequestParams)
	 * @param params
	 */
	public int getLocation(LocationRequestParams params) {
		int correlationId = 0;
		try {
			correlationId = service.getLocation(apiKey, params);
		} catch (RemoteException e) {
			logError("Error calling SLS");
		}
		return correlationId;
	}

	/**
	 * Returns true if there are any recurring location readings for this app.
	 * 
	 * @see ISecureLocationService#isPinging(String)
	 */
	public boolean isPinging() {
		try {
			return service.isPinging(apiKey);
		} catch (RemoteException e) {
			logError("Error calling SLS");
			return false;
		}
	}

	/**
	 * Cancels all scheduled readings for this app.
	 * 
	 * @see ISecureLocationService#cancelReadings(String)
	 */
	public void cancelReadings() {
		try {
			service.cancelReadings(apiKey);
		} catch (RemoteException e) {
			logError("Error calling SLS");
		}
	}

	/**
	 * Retrieves stored location data from the SLS. Once retrieved, the data is
	 * released by the SLS and cannot be retrieved again.
	 * 
	 * @see ISecureLocationService#getData(String)
	 */
	public List<LocationData> getData() {
		List<LocationData> data;
		try {
			data = service.getData(apiKey);
		} catch (RemoteException e) {
			logError("Failed to retrieve location data");
			return null;
		}
		return data;
	}

	private void logDebug(String msg) {
		try {
			Log.d(SLSServiceConnection.CLIENT_LOG_TAG, msg);
		} catch (Exception e) {
			// nevermind
		}
	}

	private void logError(String msg) {
		try {
			Log.e(SLSServiceConnection.CLIENT_LOG_TAG, msg);
		} catch (Exception e) {
			// nevermind
		}
	}

}

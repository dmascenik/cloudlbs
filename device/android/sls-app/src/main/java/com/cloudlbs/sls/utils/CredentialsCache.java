package com.cloudlbs.sls.utils;

import java.util.HashMap;
import java.util.Map;

import com.cloudlbs.sls.event.ActivationListener;
import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.ShutdownEvent;
import com.cloudlbs.sls.event.StartupEvent;
import com.cloudlbs.sls.protocol.AppDetailsProto.AppDetailsMessage;

/**
 * Contains information about apps that are currently authenticated with the
 * SLS, including the current authenticated user (if any) for each app.
 * 
 * @author Dan Mascenik
 * 
 */
public class CredentialsCache implements ActivationListener {

	private Map<String, AppDetailsMessage> appsByApiKey = new HashMap<String, AppDetailsMessage>();
	private Map<String, UserCredentials> userCredentialsByApiKey = new HashMap<String, UserCredentials>();

	public CredentialsCache() {
		EventDispatcher.addListener(this);
	}

	@Override
	public void onShutdown(ShutdownEvent evt) {
		clear();
	}

	@Override
	public void onStartupEvent(StartupEvent evt) {
		clear();
	}

	private void clear() {
		appsByApiKey.clear();
		userCredentialsByApiKey.clear();
	}

	public void addAppDetails(AppDetailsMessage appDetails) {
		appsByApiKey.put(appDetails.getApiKey(), appDetails);
	}

	public void removeAppDetails(AppDetailsMessage appDetails) {
		appsByApiKey.remove(appDetails.getApiKey());
	}

	public synchronized AppDetailsMessage getAppDetails(String apiKey) {
		return appsByApiKey.get(apiKey);
	}

	public void addUserCredentials(String apiKey, UserCredentials user) {
		userCredentialsByApiKey.put(apiKey, user);
	}

	public void removeUserCredentials(String apiKey) {
		userCredentialsByApiKey.remove(apiKey);
	}

	public synchronized UserCredentials getUserCredentials(String apiKey) {
		return userCredentialsByApiKey.get(apiKey);
	}

	public synchronized void logout(String apiKey) {
		removeUserCredentials(apiKey);
		appsByApiKey.remove(apiKey);
	}

}

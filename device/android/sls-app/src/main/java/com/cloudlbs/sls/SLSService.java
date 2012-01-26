package com.cloudlbs.sls;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.binary.Hex;

import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;

import com.cloudlbs.sls.core.AuthenticationStatus;
import com.cloudlbs.sls.core.LocationData;
import com.cloudlbs.sls.core.LocationRequestParams;
import com.cloudlbs.sls.core.SLSStatus;
import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.ShutdownEvent;
import com.cloudlbs.sls.event.StartupEvent;
import com.cloudlbs.sls.location.CancelReadingRequest;
import com.cloudlbs.sls.location.ReadingRequest;
import com.cloudlbs.sls.protocol.AppDetailsProto.AppDetailsMessage;
import com.cloudlbs.sls.protocol.AppDetailsProto.AppDetailsMessage.Builder;
import com.cloudlbs.sls.protocol.AuthenticationProto.AuthenticationMessage;
import com.cloudlbs.sls.utils.CredentialsCache;
import com.cloudlbs.sls.utils.Logger;
import com.cloudlbs.sls.utils.StringUtils;
import com.cloudlbs.sls.utils.UserCredentials;

/**
 * This service is the main hook into the core SLS API. This class must be
 * registered as a service in the AndroidManifest.xml for this app.
 * 
 * @author Dan Mascenik
 */
public final class SLSService extends BaseSLSService {

	/**
	 * Returns the operational status of the SLS, indicating if it is on, has
	 * network connectivity, is connected to a server, etc.
	 */
	public SLSStatus getStatus() {
		return new SLSStatus(processorClient.getStatus());
	}

	/**
	 * Indicates if the user is authenticated with the given app. Apps that do
	 * not use user accounts will always return false.
	 * 
	 * @param apiKey
	 * @param username
	 */
	public boolean isAuthenticated(String apiKey, String username) {
		UserCredentials uc = credentialsCache.getUserCredentials(apiKey);
		boolean result = false;
		if (uc != null && uc.getUsername().equals(username)) {
			if (isNetworkAvailable()
					&& uc.getExpirationTime() < System.currentTimeMillis()) {
				// Expired, need to recheck
				AppDetailsMessage appDetails = getAppDetails(apiKey);
				AuthenticationStatus authStatus = authenticate(apiKey,
						username, uc.getPassword(), appDetails);
				if (authStatus.getIsSuccessful()
						|| authStatus.getFaultCode() == AuthenticationStatus.FAULT_SERVICE_FAILURE) {
					result = true;
				} else {
					result = false;
				}
			} else {
				result = true;
			}
		}
		return result;
	}

	/**
	 * Wrapper for {@link #authenticate(String, String, String)} that also
	 * performs caller validation.
	 * 
	 * @param callingUID
	 * @param apiKey
	 * @param username
	 * @param password
	 */
	public AuthenticationStatus authenticate(int callingUID, String apiKey,
			String username, String password) {

		AuthenticationStatus auth = new AuthenticationStatus();
		auth.setIsSuccessful(false);
		if (!isEnabled()) {
			auth.setFaultCode(AuthenticationStatus.FAULT_SERVICE_DISABLED);
			return auth;
		}
		if (!isApiKeyValid(callingUID, apiKey)) {
			if (!isNetworkAvailable()) {
				auth.setFaultCode(AuthenticationStatus.FAULT_NO_NETWORK);
			} else {
				auth.setFaultCode(AuthenticationStatus.FAULT_INVALID_API_KEY);
			}
			return auth;
		} else {
			AppDetailsMessage appDetails = getAppDetails(apiKey);
			if (!appDetails.getUsesUserAccounts()) {
				Logger.error("Cannot authenticate with an app that does not use user accounts");
				auth.setFaultCode(AuthenticationStatus.FAULT_INVALID_APP);
				return auth;
			} else {
				auth = authenticate(apiKey, username, password, appDetails);
			}
		}
		return auth;
	}

	/**
	 * Authenticates the user but performs no caller authentication. This should
	 * only be used by a locally bound service.
	 * 
	 * @param apiKey
	 *            if null or empty, defaults to <code>SYSTEM_IDENTIFIER</code>
	 * @param username
	 * @param password
	 * @param appDetails
	 *            may be null only if apiKey is null
	 */
	public AuthenticationStatus authenticate(String apiKey, String username,
			String password, AppDetailsMessage appDetails) {

		// Fail fast if the service is off
		AuthenticationStatus auth = new AuthenticationStatus();
		auth.setIsSuccessful(false);
		if (!isEnabled()) {
			auth.setFaultCode(AuthenticationStatus.FAULT_SERVICE_DISABLED);
			return auth;
		}

		String appName;
		String appGuid;
		if (StringUtils.isBlank(apiKey) || apiKey.equals(SYSTEM_IDENTIFIER)) {
			apiKey = SYSTEM_IDENTIFIER;
			appName = SYSTEM_IDENTIFIER;
			appGuid = SYSTEM_IDENTIFIER;
		} else {
			appName = appDetails.getAppName();
			appGuid = appDetails.getAppGuid();
		}

		// do-while(true) for fast breakout
		do {
			// Try to use credentials from the cache first
			UserCredentials cachedCredentials = credentialsCache
					.getUserCredentials(apiKey);
			if (cachedCredentials != null) {
				Logger.debug("Credential cache hit for " + username
						+ "/******* for " + appName);
				if (cachedCredentials.getExpirationTime() > System
						.currentTimeMillis()) {
					AuthenticationStatus as = validateCachedUserCredentials(
							cachedCredentials, username, password, appName);
					if (as.getIsSuccessful()) {
						auth = as;
						break;
					}
				} else {
					Logger.debug("Credentials expired for " + username
							+ "/******* for " + appName);
				}
			} else {
				Logger.debug("Credential cache miss for " + username
						+ "/******* for " + appName);
			}

			// Cache is empty or expired, authenticate over the network
			if (isNetworkAvailable()) {
				try {
					auth = validateUserCredentialsViaNetwork(appGuid, appName,
							apiKey, username, password);
					break;
				} catch (Exception e) {
					Logger.error("Authentication service call failed", e);
					auth.setFaultCode(AuthenticationStatus.FAULT_SERVICE_FAILURE);
				}
			}

			// Network unavailable or call failed, try expired item from cache
			if (cachedCredentials != null) {
				auth = validateCachedUserCredentials(cachedCredentials,
						username, password, appName);
				break;
			}

			// Only possible faults are service call failure or no network
			if (!isNetworkAvailable()) {
				auth.setFaultCode(AuthenticationStatus.FAULT_NO_NETWORK);
				break;
			} else {
				// double check that this should only be a service failure
				if (auth.getFaultCode() != AuthenticationStatus.FAULT_SERVICE_FAILURE) {
					throw new RuntimeException("Unexpected fault code");
				}
			}

			break;
		} while (true);

		return auth;
	}

	/**
	 * 
	 * @param credentials
	 * @param username
	 * @param password
	 * @param appName
	 */
	private AuthenticationStatus validateCachedUserCredentials(
			UserCredentials credentials, String username, String password,
			String appName) {
		AuthenticationStatus auth = new AuthenticationStatus();
		auth.setIsSuccessful(false);
		if (credentials.getUsername().equals(username)
				&& credentials.getPassword().equals(password)) {
			Logger.debug("Authentication with cached credentials success: "
					+ username + "/******* for " + appName);
			auth.setIsSuccessful(true);
		} else {
			Logger.debug("Authentication with cached credentials FAILED: "
					+ username + "/******* for " + appName);
			auth.setFaultCode(AuthenticationStatus.FAULT_CREDENTIALS);
		}
		return auth;
	}

	/**
	 * Note that if successful, this will replace the {@link UserCredentials} in
	 * the {@link CredentialsCache}. If unsuccessful, the
	 * {@link UserCredentials} will be cleared. An exception may be thrown if
	 * the remote service call fails. If this occurs, no credentials are updated
	 * or cleared.
	 * 
	 * @param appGuid
	 * @param appName
	 * @param apiKey
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	private AuthenticationStatus validateUserCredentialsViaNetwork(
			String appGuid, String appName, String apiKey, String username,
			String password) throws Exception {
		Logger.debug("Authenticating over network for " + username
				+ "/******* for " + appName);

		AuthenticationMessage.Builder ab = AuthenticationMessage.newBuilder();
		ab.setDeviceUniqueId(phoneInfoDao.getDeviceUniqueId());
		ab.setAppGuid(appGuid);
		ab.setUsername(username);
		ab.setPassword(password);

		AuthenticationStatus auth = new AuthenticationStatus();
		auth.setIsSuccessful(false);

		AuthenticationMessage result = authenticationRemoteService.create(ab
				.build());
		if (result.getSuccess()) {
			auth.setIsSuccessful(true);
			UserCredentials uc = new UserCredentials(username, password,
					result.getToken());

			Logger.debug("Authentication over network success: " + username
					+ "/******* for " + appName);
			credentialsCache.addUserCredentials(apiKey, uc);
		} else {
			Logger.debug("Authentication over network FAILED for " + username
					+ "/******* for " + appName);
			credentialsCache.removeUserCredentials(apiKey);
			boolean badCredentials = result.getBadCredentials();
			boolean userDisabled = result.getUserDisabled();
			boolean appDisabled = result.getAppDisabled();
			if (badCredentials) {
				auth.setFaultCode(AuthenticationStatus.FAULT_CREDENTIALS);
			}
			if (userDisabled) {
				auth.setFaultCode(AuthenticationStatus.FAULT_USER_DISABLED);
			}
			if (appDisabled) {
				auth.setFaultCode(AuthenticationStatus.FAULT_APP_DISABLED);
			}
		}
		return auth;
	}

	public void logout(String apiKey) {
		verifyUserLogin(apiKey);
		credentialsCache.logout(apiKey);
	}

	/**
	 * Schedules a location reading.
	 * 
	 * @param apiKey
	 *            Defaults to <code>SYSTEM_IDENTIFIER</code> if null
	 */
	public int getLocation(String apiKey, LocationRequestParams params) {
		if (apiKey == null) {
			apiKey = SYSTEM_IDENTIFIER;
		} else {
			verifyUserLogin(apiKey);
		}
		ReadingRequest req = new ReadingRequest(apiKey, params);
		EventDispatcher.dispatchEvent(req);

		// TODO return correlation ID
		return 0;
	}

	/**
	 * Returns true if the app is currently running a recurring location reading
	 * 
	 * @param apiKey
	 *            Defaults to <code>SYSTEM_IDENTIFIER</code> if null
	 */
	public boolean isPinging(String apiKey) {
		if (apiKey == null) {
			apiKey = SYSTEM_IDENTIFIER;
		} else {
			verifyUserLogin(apiKey);
		}
		boolean isPinging = false;
		Collection<ReadingRequest> requests = manager
				.getReadingRequests(apiKey);
		Iterator<ReadingRequest> iter = requests.iterator();
		while (iter.hasNext()) {
			ReadingRequest req = iter.next();
			if (req.isRecurring()) {
				isPinging = true;
				break;
			}
		}
		return isPinging;
	}

	/**
	 * Cancels all pending readings for a given API key
	 * 
	 * @param apiKey
	 *            Defaults to <code>SYSTEM_IDENTIFIER</code> if null
	 */
	public void cancelReadings(String apiKey) {
		if (apiKey == null) {
			apiKey = SYSTEM_IDENTIFIER;
		} else {
			verifyUserLogin(apiKey);
		}
		EventDispatcher.dispatchEvent(new CancelReadingRequest(apiKey));
	}

	/**
	 * Gets any stored location data for the calling app.
	 * 
	 * @param apiKey
	 */
	public List<LocationData> getLocationData(String apiKey) {
		verifyUserLogin(apiKey);
		return locationDataBuffer.getLocationData(getAppDetails(apiKey));
	}

	/**
	 * Is the SLS running? This does not indicate whether or not there is
	 * network connectivity or GPS.
	 */
	public boolean isEnabled() {
		return preferencesDao.getEnabled();
	}

	/**
	 * Restore any preferences to the default values
	 */
	public void restoreDefaults() {
		setEnabled(false);
		preferencesDao.clear();
		Logger.info("All stored settings reset");
	}

	/**
	 * Returns true if the state change completed without errors
	 * 
	 * @param nowEnabled
	 */
	public void setEnabled(boolean nowEnabled) {
		boolean wasEnabled = isEnabled();
		preferencesDao.setEnabled(nowEnabled);
		if (wasEnabled && !nowEnabled) {
			// need to shut down
			try {
				unregisterReceiver(connectivityReceiver);
			} catch (Exception e) {
				Logger.warn("Failed to unregister receiver - already done?");
			}
			EventDispatcher.dispatchEvent(new ShutdownEvent());
		} else if (!wasEnabled && nowEnabled) {
			// need to start up
			registerReceiver(connectivityReceiver, new IntentFilter(
					ConnectivityManager.CONNECTIVITY_ACTION));
			EventDispatcher.dispatchEvent(new StartupEvent());
		} else {
			// not a condition change - do nothing
		}
	}

	/**
	 * Returns the current contents of the log buffer
	 */
	public String getLogContents() {
		return logBuffer.getContents();
	}

	public String getMasterHostname() {
		return preferencesDao.getMasterHostname();
	}

	public void setMasterHostname(String hostname) {
		preferencesDao.setMasterHostname(hostname);
	}

	public int getMasterPort() {
		return preferencesDao.getMasterPort();
	}

	public void setMasterPort(int port) {
		preferencesDao.setMasterPort(port);
	}

	public boolean getUseHttps() {
		return preferencesDao.getUseHttps();
	}

	public void setUseHttps(boolean useHttps) {
		preferencesDao.setUseHttps(useHttps);
	}

	public int getLogBufferSize() {
		return preferencesDao.getLogBufferSize();
	}

	public void setLogBufferSize(int size) {
		logBuffer.setSize(size);
		preferencesDao.setLogBufferSize(size);
	}

	public boolean getEmulatorMode() {
		return preferencesDao.getEmulatorMode();
	}

	public void setEmulatorMode(boolean isEmulatorMode) {
		preferencesDao.setEmulatorMode(isEmulatorMode);
	}

	/**
	 * Checks whether the app associated with this key requires a user to be
	 * authenticated, and verifies the authentication if it does.
	 * 
	 * @param apiKey
	 */
	public void verifyUserLogin(String apiKey) {
		if (StringUtils.isBlank(apiKey)) {
			throw new RuntimeException("Empty API key");
		}
		AppDetailsMessage appDetails = getAppDetails(apiKey);
		if (appDetails == null || !appDetails.getIsValid()) {
			throw new RuntimeException("Invalid apiKey: " + apiKey);
		}
		if (appDetails.getUsesUserAccounts()) {
			UserCredentials uc = credentialsCache.getUserCredentials(apiKey);
			if (uc == null || !isAuthenticated(apiKey, uc.getUsername())) {
				throw new RuntimeException("Not authenticated");
			}
		}
	}

	/**
	 * Checks if the API key is valid for the caller and throws an exception if
	 * it isn't.
	 * 
	 * @param callingUID
	 * @param apiKey
	 */
	public void validateApiKey(int callingUID, String apiKey) {
		if (!isApiKeyValid(callingUID, apiKey)) {
			throw new RuntimeException("Invalid API key");
		}
	}

	/**
	 * Validates the API key for the caller
	 * 
	 * @param callingUID
	 * @param apiKey
	 */
	public boolean isApiKeyValid(int callingUID, String apiKey) {
		if (StringUtils.isBlank(apiKey)) {
			return false;
		}
		PackageManager pm = getPackageManager();
		String packageName = pm.getNameForUid(callingUID);
		String certHex = getCertificateHex(packageName);
		AppDetailsMessage appDetails = getAppDetails(apiKey, certHex,
				packageName);
		if (appDetails != null && appDetails.getIsValid()) {
			return true;
		} else {
			return false;
		}
	}

	private String getCertificateHex(String packageName) {
		String hex = null;
		try {
			Signature s = getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_SIGNATURES).signatures[0];
			CertificateFactory f = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) f
					.generateCertificate(new ByteArrayInputStream(s
							.toByteArray()));
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(cert.getEncoded());
			hex = new String(Hex.encodeHex(md5.digest())).toUpperCase();
		} catch (Exception e) {
			Logger.error("Certificate extraction failed for " + packageName);
		}
		return hex;
	}

	public AppDetailsMessage getAppDetails(String apiKey) {
		if (StringUtils.isBlank(apiKey)) {
			throw new RuntimeException("Empty API key");
		}
		AppDetailsMessage adm = credentialsCache.getAppDetails(apiKey);
		if (adm == null || !adm.getIsValid()) {
			throw new RuntimeException("Invalid API key");
		}
		return adm;
	}

	public AppDetailsMessage getAppDetails(String apiKey, String certHex,
			String packageName) {
		if (StringUtils.isBlank(apiKey)) {
			return null;
		}
		AppDetailsMessage result = credentialsCache.getAppDetails(apiKey);
		boolean refresh = false;
		if (result != null) {
			if (isNetworkAvailable()
					&& result.getApiKeyExpirationDate() < System
							.currentTimeMillis()) {
				refresh = true;
			}
		} else {
			refresh = true;
		}

		if (refresh) {
			try {
				Builder inBuilder = AppDetailsMessage.newBuilder();
				inBuilder.setApiKey(apiKey);
				inBuilder.setAppIdentifier(packageName);
				inBuilder.setCertificateFingerprint(certHex);
				inBuilder.setDeviceUniqueId(phoneInfoDao.getDeviceUniqueId());

				AppDetailsMessage newDetails = appDetailsRemoteService
						.create(inBuilder.build());

				Builder resultBuilder = AppDetailsMessage
						.newBuilder(newDetails);

				long fifteenMinutesFromNow = System.currentTimeMillis()
						+ (15 * 60 * 1000);
				long cacheExpiry = fifteenMinutesFromNow;
				if (newDetails.hasApiKeyExpirationDate()) {
					long expiration = newDetails.getApiKeyExpirationDate();
					if (expiration < fifteenMinutesFromNow) {
						cacheExpiry = expiration;
					}
				}
				resultBuilder.setApiKeyExpirationDate(cacheExpiry);
				result = resultBuilder.build();

				credentialsCache.addAppDetails(result);
			} catch (Exception e) {
				Logger.error("Failed to load app details", e);
			}
		}
		return result;
	}

}

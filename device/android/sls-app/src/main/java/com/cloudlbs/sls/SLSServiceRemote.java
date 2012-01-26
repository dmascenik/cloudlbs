package com.cloudlbs.sls;

import java.util.List;

import android.os.RemoteException;

import com.cloudlbs.sls.core.ISecureLocationService;
import com.cloudlbs.sls.core.AuthenticationStatus;
import com.cloudlbs.sls.core.LocationData;
import com.cloudlbs.sls.core.LocationRequestParams;
import com.cloudlbs.sls.core.SLSStatus;

/**
 * This is the backend for the {@link ISecureLocationService} AIDL binding. It
 * simply passes a select set of operations through to the underlying
 * {@link SLSService}.<br/>
 * <br/>
 * All calls via this interface are expected to provide an {@link AppKey} and
 * {@link AuthToken} token for authentication by the SLS.<br/>
 * <br/>
 * The {@link SLSService} provides many more operations than this interface
 * exposes. Exercise caution when exposing {@link SLSService} operations via
 * this interface since there may be security implications.
 * 
 * @author Dan Mascenik
 * 
 */
public class SLSServiceRemote extends ISecureLocationService.Stub {

	private SLSService secureLocationService;

	SLSServiceRemote(SLSService secureLocationService) {
		this.secureLocationService = secureLocationService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cloudlbs.sls.core.ISecureLocationService#isEnabled()
	 */
	@Override
	public boolean isEnabled() throws RemoteException {
		return secureLocationService.isEnabled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cloudlbs.core.sls.android.ISecureLocationService#getStatus()
	 */
	@Override
	public SLSStatus getStatus() throws RemoteException {
		return secureLocationService.getStatus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cloudlbs.core.sls.android.ISecureLocationService#isAuthenticated(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isAuthenticated(String apiKey, String username)
			throws RemoteException {
		return secureLocationService.isApiKeyValid(getCallingUid(), apiKey)
				& secureLocationService.isAuthenticated(apiKey, username);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cloudlbs.core.sls.android.ISecureLocationService#authenticate
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public AuthenticationStatus authenticate(String apiKey, String username,
			String password) throws RemoteException {
		return secureLocationService.authenticate(getCallingUid(), apiKey,
				username, password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cloudlbs.core.sls.android.ISecureLocationService#logout(java.lang
	 * .String)
	 */
	@Override
	public void logout(String apiKey) throws RemoteException {
		secureLocationService.validateApiKey(getCallingUid(), apiKey);
		secureLocationService.logout(apiKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cloudlbs.core.sls.android.ISecureLocationService#getLocation(java
	 * .lang.String, com.cloudlbs.core.sls.android.LocationRequestParams)
	 */
	@Override
	public int getLocation(String apiKey, LocationRequestParams params)
			throws RemoteException {
		secureLocationService.validateApiKey(getCallingUid(), apiKey);
		return secureLocationService.getLocation(apiKey, params);
	}

	@Override
	public void cancelReadings(String apiKey) throws RemoteException {
		secureLocationService.validateApiKey(getCallingUid(), apiKey);
		secureLocationService.cancelReadings(apiKey);
	}

	@Override
	public boolean isPinging(String apiKey) throws RemoteException {
		secureLocationService.validateApiKey(getCallingUid(), apiKey);
		return secureLocationService.isPinging(apiKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cloudlbs.core.sls.android.ISecureLocationService#getData(java.lang
	 * .String)
	 */
	@Override
	public List<LocationData> getData(String apiKey) throws RemoteException {
		secureLocationService.validateApiKey(getCallingUid(), apiKey);
		return secureLocationService.getLocationData(apiKey);
	}

}

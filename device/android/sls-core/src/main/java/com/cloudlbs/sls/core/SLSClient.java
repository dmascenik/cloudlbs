package com.cloudlbs.sls.core;

/**
 * @author Dan Mascenik
 * 
 */
public interface SLSClient {

	public void onSLSServiceConnected(SecureLocationService service);

	public void onSLSServiceDisconnected();

	/**
	 * Called when a system broadcast for this app is received. The app is
	 * expected to call back the SLS to retrieve waiting data.
	 */
	public void onSLSBroadcast();

}

package com.cloudlbs.sls.http;

import com.cloudlbs.sls.dao.PreferencesDao;
import com.cloudlbs.sls.protocol.DeviceConnectionProto.DeviceConnectionMessage;

/**
 * Remote service for getting device connection information such as what XMPP
 * credentials to use, and what processor to communicate with.
 * 
 * @author Dan Mascenik
 * 
 */
public class DeviceRegistrationRemoteService extends
		BaseRemoteService<DeviceConnectionMessage> {

	public DeviceRegistrationRemoteService(PreferencesDao preferencesDao) {
		super(preferencesDao, "/svc/device/connection");
	}

}

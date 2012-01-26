package com.cloudlbs.sls.http;

import com.cloudlbs.sls.dao.PreferencesDao;
import com.cloudlbs.sls.protocol.AuthenticationProto.AuthenticationMessage;

/**
 * Remote service for authenticating users for an app on a device
 * 
 * @author Dan Mascenik
 * 
 */
public class AuthenticationRemoteService extends
		BaseRemoteService<AuthenticationMessage> {

	public AuthenticationRemoteService(PreferencesDao preferencesDao) {
		super(preferencesDao, "/svc/device/auth");
	}

}

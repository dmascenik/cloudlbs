package com.cloudlbs.sls.http;

import com.cloudlbs.sls.dao.PreferencesDao;
import com.cloudlbs.sls.protocol.AppDetailsProto.AppDetailsMessage;

/**
 * Accesses app details by API key.
 * 
 * @author Dan Mascenik
 * 
 */
public class AppDetailsRemoteService extends
		BaseRemoteService<AppDetailsMessage> {

	public AppDetailsRemoteService(PreferencesDao preferencesDao) {
		super(preferencesDao, "/svc/device/app");
	}

}

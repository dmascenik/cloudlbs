package com.cloudlbs.sls.http;

import com.cloudlbs.sls.dao.PreferencesDao;
import com.cloudlbs.sls.protocol.LocationProto.LocationDataMessage;

/**
 * Remote service for posting location readings to the server
 * 
 * @author Dan Mascenik
 * 
 */
public class LocationRemoteService extends
		BaseRemoteService<LocationDataMessage> {

	public LocationRemoteService(PreferencesDao preferencesDao) {
		super(preferencesDao, "/svc/location");
	}

}

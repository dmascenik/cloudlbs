package com.cloudlbs.core.utils.test;

import com.cloudlbs.core.utils.remote.RestProtobufRemoteService;
import com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage;

/**
 * @author Dan Mascenik
 * 
 */
public class FixedLocationRemoteService extends
		RestProtobufRemoteService<FixedLocationMessage, FixedLocation> {

	public FixedLocationRemoteService() {
		super(new FixedLocationMessageConverter(), FixedLocationMessage.items);
	}

}

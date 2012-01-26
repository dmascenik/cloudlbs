package com.cloudlbs.core.utils.test;

import com.cloudlbs.core.utils.remote.RestProtobufRemoteService;
import com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage;

/**
 * @author Dan Mascenik
 * 
 */
public class NonConvertingFixedLocationRemoteService extends
		RestProtobufRemoteService<FixedLocationMessage, FixedLocationMessage> {

	public NonConvertingFixedLocationRemoteService() {
		super(null, FixedLocationMessage.items);
	}

}

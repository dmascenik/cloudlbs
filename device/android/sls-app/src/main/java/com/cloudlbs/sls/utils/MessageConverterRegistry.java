package com.cloudlbs.sls.utils;

import com.cloudlbs.sls.location.LocationDataCollection;
import com.cloudlbs.sls.location.LocationReading;
import com.cloudlbs.sls.protocol.LocationProto.LocationDataMessage;
import com.cloudlbs.sls.protocol.LocationProto.LocationReadingMessage;

/**
 * @author Dan Mascenik
 * 
 */
public class MessageConverterRegistry {

	public MessageConverterRegistry() {

		new ProtobufMessageConverter<LocationReadingMessage, LocationReading>() {
		};

		new ProtobufMessageConverter<LocationDataMessage, LocationDataCollection>() {
		};

	}

}

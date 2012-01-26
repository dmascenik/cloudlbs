package com.cloudlbs.platform.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudlbs.platform.domain.LocationReading;
import com.cloudlbs.platform.xmpp.LocationReadingListener;
import com.cloudlbs.platform.xmpp.OutboundMessageSender;
import com.cloudlbs.sls.protocol.LocationProto.LocationDataMessage;
import com.cloudlbs.sls.protocol.LocationProto.LocationReadingMessage;

/**
 * Temporary service that shares locations between hard-coded devices.
 * 
 * @author Dan Mascenik
 * 
 */
@Service("hackeyLocationSharingService")
public class HackeyLocationSharingService implements LocationReadingListener {

	private static Map<String, String> peoplesDevices = new HashMap<String, String>();

	private static final String EMULATOR_ID = "ANDROID-000000000000000";

	static {
		peoplesDevices.put(EMULATOR_ID, "Emulator");
		peoplesDevices.put("ANDROID-A0000015F7ADF9", "Dan M");
		peoplesDevices.put("ANDROID-355266040966150", "Brian W");
		// peoplesDevices.put("A0000015FA99A0-CDMA", "Shannon G");
		// peoplesDevices.put("A000002271DB9D-CDMA", "Rachel W");
		// peoplesDevices.put("A10000135322AF-CDMA", "Bill W");
		peoplesDevices.put("0000001-CDMA", "Robot 1");
		peoplesDevices.put("0000002-CDMA", "Robot 2");
	}

	@Autowired
	private OutboundMessageSender outboundMessageSender;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cloudlbs.platform.processor.listener.LocationReadingListener#
	 * onLocationReading(com.cloudlbs.platform.processor.domain.LocationReading)
	 */
	@Override
	public void onLocationReading(LocationReading locationReading) {

		String from = locationReading.getSubjGuid().toUpperCase();
		if (peoplesDevices.get(from) == null) {
			log.debug(from
					+ " not a member of the hackey location sharing pool");
			return;
		}
		for (String key : peoplesDevices.keySet()) {
			if (!key.equals(from)) { // don't echo back to sender
				LocationDataMessage.Builder b = LocationDataMessage
						.newBuilder();
				LocationReadingMessage.Builder rb = LocationReadingMessage
						.newBuilder();
				rb.setLatitude(locationReading.getLatitude());
				rb.setLongitude(locationReading.getLongitude());
				rb.setErrorRadius(locationReading.getErrorRadius());
				rb.setAltitude(locationReading.getAltitude());
				rb.setFixTime(locationReading.getFixTime());
				rb.setTimedOut(locationReading.getTimedOut());
				rb.setTimestamp(locationReading.getTimestamp());
				rb.setSubjGuid(from);

				// FIXME
				// Add the WhereRU api KEY
				if (key.equals(EMULATOR_ID)) {
					// this only applies to Dan's laptop
					rb.setApiKey("PreSDlnGVul0jTfohXgKK4gg");
				} else {
					// This only works for dev
					rb.setApiKey("k7n8SGcC0hVLML6CqdYy18wvFs");
				}

				// FIXME
				rb.setGuid(from);
				rb.setPrettyName(peoplesDevices.get(from));

				// FIXME
				rb.setAppGuid("whereru");

				b.addLocation(rb);
				LocationDataMessage ldm = b.build();

				outboundMessageSender.send(key, ldm);

				log.debug("Sent location to " + peoplesDevices.get(key));
			}
		}
	}

	private Logger log = LoggerFactory.getLogger(getClass());
}

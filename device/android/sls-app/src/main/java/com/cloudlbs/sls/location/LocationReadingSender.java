package com.cloudlbs.sls.location;

import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.LocationReadingListener;
import com.cloudlbs.sls.event.OutboundXmppMessageEvent;

/**
 * @author Dan Mascenik
 * 
 */
public class LocationReadingSender implements LocationReadingListener {

	public LocationReadingSender() {
		EventDispatcher.addListener(this);
	}

	@Override
	public void onLocationReading(LocationReading reading) {
		OutboundXmppMessageEvent xmppOut = new OutboundXmppMessageEvent(reading);
		EventDispatcher.dispatchEvent(xmppOut);
	}

}

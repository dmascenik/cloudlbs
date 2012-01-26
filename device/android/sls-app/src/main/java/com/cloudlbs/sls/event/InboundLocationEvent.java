package com.cloudlbs.sls.event;

import com.cloudlbs.sls.location.LocationDataCollection;

/**
 * @author Dan Mascenik
 * 
 */
public class InboundLocationEvent implements SLSEvent {

	private LocationDataCollection locations;

	public InboundLocationEvent(LocationDataCollection locations) {
		this.locations = locations;
	}

	public LocationDataCollection getLocations() {
		return locations;
	}

}

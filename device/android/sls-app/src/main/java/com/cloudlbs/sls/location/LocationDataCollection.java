package com.cloudlbs.sls.location;

import java.util.HashSet;
import java.util.Set;


/**
 * @author Dan Mascenik
 * 
 */
public class LocationDataCollection {

	private Set<LocationReading> locations = new HashSet<LocationReading>();

	public Set<LocationReading> getLocations() {
		return locations;
	}

	public void setLocations(Set<LocationReading> locations) {
		this.locations = locations;
	}

}

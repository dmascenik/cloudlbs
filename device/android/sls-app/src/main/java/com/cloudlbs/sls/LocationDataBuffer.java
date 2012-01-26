package com.cloudlbs.sls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cloudlbs.sls.core.ISecureLocationService;
import com.cloudlbs.sls.core.LocationData;
import com.cloudlbs.sls.protocol.AppDetailsProto.AppDetailsMessage;

/**
 * Contains {@link LocationData} to be picked up by registered apps via
 * {@link ISecureLocationService#getData(String)}. This could be the current
 * location of this device, as well as locations of other devices and/or fixed
 * locations. Data is keyed on the app identifier. On Android, this is the
 * package name.
 * 
 * @author Dan Mascenik
 * 
 */
public class LocationDataBuffer {

	/**
	 * Location data is stored in a HashMap keyed on the guid of the item
	 * associated with that location. This way, no more than one location is
	 * stored for a single item at a time.
	 */
	private Map<String, HashMap<String, LocationData>> data = new ConcurrentHashMap<String, HashMap<String, LocationData>>();

	/**
	 * Adds another {@link LocationData} to the list for this appKey. The most
	 * recent location is always added to the end of the list.
	 * 
	 * @param appKey
	 * @param loc
	 */
	public void addLocation(AppDetailsMessage appDetails, LocationData loc) {
		HashMap<String, LocationData> locs = getDataForKey(appDetails
				.getAppIdentifier());
		locs.put(loc.getSubjGuid(), loc);
	}

	/**
	 * Adds all the {@link LocationData} elements in the list to the appKey's
	 * list.
	 * 
	 * @param appKey
	 * @param locationDataBuffer
	 */
	public void addLocations(AppDetailsMessage appDetails,
			List<LocationData> locationData) {
		HashMap<String, LocationData> locs = getDataForKey(appDetails
				.getAppIdentifier());
		for (LocationData ld : locationData) {
			locs.put(ld.getSubjGuid(), ld);
		}
	}

	/**
	 * Gets the data for the app and clears the list.
	 * 
	 * @param appDetails
	 */
	public synchronized List<LocationData> getLocationData(
			AppDetailsMessage appDetails) {
		String key = appDetails.getAppIdentifier();
		HashMap<String, LocationData> ld = getDataForKey(key);
		data.remove(key);
		return new ArrayList<LocationData>(ld.values());
	}

	private synchronized HashMap<String, LocationData> getDataForKey(
			String appIdentifier) {
		HashMap<String, LocationData> locs = data.get(appIdentifier);
		if (locs == null) {
			locs = new HashMap<String, LocationData>();
			data.put(appIdentifier, locs);
		}
		return locs;
	}

}

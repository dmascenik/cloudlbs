package com.cloudlbs.sls;

import java.util.HashSet;
import java.util.Set;

import android.content.Intent;

import com.cloudlbs.sls.core.LocationData;
import com.cloudlbs.sls.core.SLSServiceConnection;
import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.InboundLocationEvent;
import com.cloudlbs.sls.event.InboundLocationListener;
import com.cloudlbs.sls.event.LocationReadingListener;
import com.cloudlbs.sls.location.LocationDataCollection;
import com.cloudlbs.sls.location.LocationReading;
import com.cloudlbs.sls.protocol.AppDetailsProto.AppDetailsMessage;
import com.cloudlbs.sls.utils.Logger;

/**
 * This class listens for {@link LocationData} coming from any source (local
 * readings or inbound XMPP).
 * 
 * @author Dan Mascenik
 * 
 */
public class LocationDataBroadcaster implements LocationReadingListener,
		InboundLocationListener {

	private BaseSLSService context;
	private LocationDataBuffer locationData;

	public LocationDataBroadcaster(BaseSLSService context,
			LocationDataBuffer locationData) {
		this.context = context;
		this.locationData = locationData;
		EventDispatcher.addListener(this);
	}

	/**
	 * Handles location readings originating on this device.
	 */
	@Override
	public void onLocationReading(LocationReading reading) {
		Logger.debug("Notifying other apps of location reading");

		LocationData loc = new LocationData(reading.getLatitude(),
				reading.getLongitude(), reading.getAltitude(),
				reading.getErrorRadius(), reading.getTimestamp());
		/*
		 * loc.setGuid(String) left null since the reading originated on this
		 * device
		 */
		loc.setFixTime(reading.getFixTime());
		loc.setFixTimedOut(reading.getTimedOut());

		String apiKey = reading.getApiKey();
		if (apiKey != null && !apiKey.equals(SLSService.SYSTEM_IDENTIFIER)) {
			SLSService service = (SLSService) context;
			AppDetailsMessage appDetails = service.getAppDetails(apiKey);
			if (appDetails != null) {
				locationData.addLocation(appDetails, loc);
				Intent bi = new Intent(SLSServiceConnection.NOTIFY_APPS_ACTION);

				/*
				 * On Android, the app identifier is the package name
				 */
				bi.putExtra(SLSServiceConnection.APP_IDENTIFIER,
						appDetails.getAppIdentifier());
				context.sendBroadcast(bi);
			} else {
				Logger.warn("Could not validate target app API key - not broadcasting");
			}
		}
	}

	/**
	 * Handles incoming location readings from other devices.
	 */
	@Override
	public void onInboundLocationData(InboundLocationEvent evt) {
		LocationDataCollection locations = evt.getLocations();
		Set<AppDetailsMessage> appsToNotify = new HashSet<AppDetailsMessage>();
		for (LocationReading reading : locations.getLocations()) {
			if (reading.getSubjGuid() == null) {
				/*
				 * Incoming location data cannot have a null GUID. This would
				 * mean that another device did a location reading for this
				 * device, which is impossible.
				 */
				Logger.warn("Ignoring incoming location with null GUID");
				continue;
			}

			String apiKey = reading.getApiKey();
			if (apiKey != null && !apiKey.equals(SLSService.SYSTEM_IDENTIFIER)) {
				SLSService service = (SLSService) context;
				AppDetailsMessage appDetails = service.getAppDetails(apiKey);
				if (appDetails != null) {
					LocationData loc = new LocationData(reading.getLatitude(),
							reading.getLongitude(), reading.getAltitude(),
							reading.getErrorRadius(), reading.getTimestamp());
					loc.setSubjGuid(reading.getSubjGuid());
					loc.setGuid(reading.getGuid());
					loc.setPrettyName(reading.getPrettyName());
					loc.setFixTime(reading.getFixTime());
					loc.setFixTimedOut(reading.getTimedOut());
					locationData.addLocation(appDetails, loc);
					appsToNotify.add(appDetails);
				} else {
					Logger.warn("Could not validate target app API key - not broadcasting");
				}
			}
		}

		// Send out all the broadcasts
		for (AppDetailsMessage appDetails : appsToNotify) {
			Intent bi = new Intent(SLSServiceConnection.NOTIFY_APPS_ACTION);
			bi.putExtra(SLSServiceConnection.APP_IDENTIFIER,
					appDetails.getAppIdentifier());
			context.sendBroadcast(bi);
		}
	}
}

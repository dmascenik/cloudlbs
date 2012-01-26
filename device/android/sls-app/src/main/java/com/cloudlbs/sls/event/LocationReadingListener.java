package com.cloudlbs.sls.event;

import com.cloudlbs.sls.location.LocationReading;

/**
 * @author Dan Mascenik
 * 
 */
public interface LocationReadingListener {

	public void onLocationReading(LocationReading reading);

}

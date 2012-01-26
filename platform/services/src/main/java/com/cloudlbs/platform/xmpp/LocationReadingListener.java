package com.cloudlbs.platform.xmpp;

import com.cloudlbs.platform.domain.LocationReading;

/**
 * @author Dan Mascenik
 * 
 */
public interface LocationReadingListener {

	public void onLocationReading(LocationReading locationReading);

}

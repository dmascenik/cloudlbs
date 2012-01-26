package com.cloudlbs.sls.location;

import com.cloudlbs.sls.event.SLSEvent;
import com.cloudlbs.sls.utils.Assert;

/**
 * Encapsulates a location reading requested for a particular time.
 * 
 * @author Dan Mascenik
 * 
 */
public class CancelReadingRequest implements SLSEvent {

	private String apiKey;

	public CancelReadingRequest(String apiKey) {
		Assert.notNull(apiKey);
		this.apiKey = apiKey;
	}

	public String getApiKey() {
		return apiKey;
	}

}

package com.cloudlbs.sls.location;

import com.cloudlbs.sls.core.LocationRequestParams;
import com.cloudlbs.sls.event.SLSEvent;
import com.cloudlbs.sls.utils.Assert;
import com.cloudlbs.sls.utils.Logger;

/**
 * Encapsulates a location reading requested for a particular time.
 * 
 * @author Dan Mascenik
 * 
 */
public class ReadingRequest implements Comparable<ReadingRequest>, SLSEvent,
		Cloneable {

	private String apiKey;
	private LocationRequestParams params;

	private long readingTime;
	private long repeatUntil = 0;
	int repeatCount = 0;

	/**
	 * Requests a location reading for an app with the provided
	 * params.
	 * 
	 * @param apiKey
	 * @param params
	 */
	public ReadingRequest(String apiKey, LocationRequestParams params) {
		Assert.notNull(apiKey);
		Assert.notNull(params);
		this.apiKey = apiKey;
		this.params = params;
		this.readingTime = System.currentTimeMillis()
				+ params.getInitialDelayMillis();
		if (params.getRecurringDurationMillis() != 0) {
			this.repeatUntil = System.currentTimeMillis()
					+ params.getRecurringDurationMillis();
		}
	}

	void setReadingTime(long readingTime) {
		this.readingTime = readingTime;
	}

	public LocationRequestParams getParams() {
		return params;
	}

	public boolean isRecurring() {
		return params.getFrequencySeconds() != 0;
	}

	public long getRepeatUntil() {
		return repeatUntil;
	}

	public String getApiKey() {
		return apiKey;
	}

	public long getReadingTime() {
		return readingTime;
	}

	/**
	 * Returns true if this request is more than
	 * {@link LocationRequestParams#REQUEST_EXPIRATION_SECONDS} past its
	 * {@link #maxWaitSeconds} time. Expired requests should be removed from the
	 * schedule since they will never be fulfilled.
	 * 
	 */
	public boolean hasExpired() {
		long now = System.currentTimeMillis();
		long requestAge = now - getReadingTime();
		if (requestAge > (getParams().getMaxWaitSeconds() * 1000 + LocationRequestParams.REQUEST_EXPIRATION_SECONDS * 1000)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if the provided location meets all the parameters of this
	 * request.
	 * 
	 * @param readingTime
	 * @param errorRadius
	 */
	public boolean accepts(long readingTime, float errorRadius) {
		return accepts(System.currentTimeMillis(), readingTime, errorRadius);
	}

	boolean accepts(long now, long readingTime, float errorRadius) {
		long requestAge = now - getReadingTime();
		long gpsAge = now - readingTime;

		if (requestAge < 0 || gpsAge > getParams().getMaxGpsAgeSeconds() * 1000) {
			/*
			 * Don't accept locations that are before this request's scheduled
			 * time or where the GPS reading is too old
			 */
			Logger.debug("Rejecting reading - too early or GPS age to old");
			return false;
		} else if (requestAge > getParams().getMaxWaitSeconds() * 1000
				|| errorRadius <= getParams().getMaxErrorMeters()) {
			/*
			 * Must accept readings if max wait has passed or if the error
			 * radius is acceptable
			 */
			Logger.debug("Accepting reading");
			return true;
		} else {
			/*
			 * Otherwise, wait for something better
			 */
			Logger.debug("Insufficient accuracy - waiting for better reading or timeout");
			return false;
		}

	}

	public ReadingRequest clone() {
		ReadingRequest clone;
		try {
			clone = (ReadingRequest) super.clone();
			LocationRequestParams paramsClone = (LocationRequestParams) params
					.clone();
			clone.params = paramsClone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ReadingRequest o) {
		if (getReadingTime() < o.getReadingTime()) {
			return -1;
		} else if (getReadingTime() > o.getReadingTime()) {
			return 1;
		} else {
			return 0;
		}
	}

}

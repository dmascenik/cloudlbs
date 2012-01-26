package com.cloudlbs.sls.location;

import com.cloudlbs.sls.event.SLSEvent;
import com.cloudlbs.sls.utils.Assert;

/**
 * @author Dan Mascenik
 * 
 */
public class LocationReading implements SLSEvent {

	private double latitude;
	private double longitude;
	private double altitude;
	private long timestamp;
	private float errorRadius;
	private int fixTime;
	private boolean timedOut;
	private String subjGuid;
	private String guid;
	private String prettyName;
	private String apiKey;
	private String appIdentifier;
	private String appGuid;
	private String certificateFingerprint;

	/**
	 * Constructor required for message converters
	 */
	public LocationReading() {
	}

	private LocationReading(double latitude, double longitude, double altitude,
			float errorRadius, long timestamp) {
		this();
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.errorRadius = errorRadius;
		this.timestamp = timestamp;
	}

	public LocationReading(String apiKey, double latitude, double longitude,
			double altitude, float errorRadius, long timestamp) {
		this(latitude, longitude, altitude, errorRadius, timestamp);
		Assert.notNull(apiKey);
		this.apiKey = apiKey;
	}

	public boolean getTimedOut() {
		return timedOut;
	}

	public void setTimedOut(boolean timedOut) {
		this.timedOut = timedOut;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public float getErrorRadius() {
		return errorRadius;
	}

	/**
	 * Time from the original reading request until the accepted GPS fix's time
	 * in millis.
	 */
	public int getFixTime() {
		return fixTime;
	}

	public void setFixTime(int fixTime) {
		this.fixTime = fixTime;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * The guid of the item whose location this is. For the current device, this
	 * is left null.
	 * 
	 */
	public String getSubjGuid() {
		return subjGuid;
	}

	public void setSubjGuid(String subjectGuid) {
		this.subjGuid = subjectGuid;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void setErrorRadius(float errorRadius) {
		this.errorRadius = errorRadius;
	}

	public String getPrettyName() {
		return prettyName;
	}

	public void setPrettyName(String prettyName) {
		this.prettyName = prettyName;
	}

	public String getAppIdentifier() {
		return appIdentifier;
	}

	public void setAppIdentifier(String appIdentifier) {
		this.appIdentifier = appIdentifier;
	}

	public String getAppGuid() {
		return appGuid;
	}

	public void setAppGuid(String appGuid) {
		this.appGuid = appGuid;
	}

	public String getCertificateFingerprint() {
		return certificateFingerprint;
	}

	public void setCertificateFingerprint(String certificateFingerprint) {
		this.certificateFingerprint = certificateFingerprint;
	}

}

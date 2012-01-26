package com.cloudlbs.platform.domain;

import com.cloudlbs.core.utils.events.Event;

public class LocationReading implements Event {

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

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public float getErrorRadius() {
		return errorRadius;
	}

	public void setErrorRadius(float errorRadius) {
		this.errorRadius = errorRadius;
	}

	public int getFixTime() {
		return fixTime;
	}

	public void setFixTime(int fixTime) {
		this.fixTime = fixTime;
	}

	public boolean getTimedOut() {
		return timedOut;
	}

	public void setTimedOut(boolean timedOut) {
		this.timedOut = timedOut;
	}

	public String getSubjGuid() {
		return subjGuid;
	}

	public void setSubjGuid(String subjGuid) {
		this.subjGuid = subjGuid;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getPrettyName() {
		return prettyName;
	}

	public void setPrettyName(String prettyName) {
		this.prettyName = prettyName;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
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

package com.cloudlbs.sls.ui;

import com.cloudlbs.sls.ui.mvp.Model;

/**
 * @author Dan Mascenik
 * 
 */
public class LastLocationModel extends Model {

	private boolean isLocationKnown;
	private double latitude;
	private double longitude;
	private double altitude;
	private float errorRadius;
	private long timestamp;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
		setHasChanges(true);
		notifyViews();
	}

	public boolean isLocationKnown() {
		return isLocationKnown;
	}

	public void setLocationKnown(boolean isLocationKnown) {
		this.isLocationKnown = isLocationKnown;
		setHasChanges(true);
		notifyViews();
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
		setHasChanges(true);
		notifyViews();
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
		setHasChanges(true);
		notifyViews();
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
		setHasChanges(true);
		notifyViews();
	}

	public float getErrorRadius() {
		return errorRadius;
	}

	public void setErrorRadius(float errorRadius) {
		this.errorRadius = errorRadius;
		setHasChanges(true);
		notifyViews();
	}

}

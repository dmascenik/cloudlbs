package com.cloudlbs.sls.core;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Dan Mascenik
 * 
 */
public final class LocationData implements Parcelable {

	private double latitude;
	private double longitude;
	private double altitude;
	private float errorRadius;
	private long timestamp;

	// Time taken to get a fix of sufficient errorRadius (in seconds)
	private int fixTime;

	// Is this the best fix achieved in the available time
	private boolean fixTimedOut;

	private String subjGuid;

	// FIXME
	private String guid;

	private String prettyName;
	private boolean fixed;

	public LocationData(double latitude, double longitude, double altitude,
			float accuracy, long timestamp) {
		if (accuracy < 0) {
			throw new IllegalArgumentException(
					"Invalid errorRadius (less than zero)");
		}
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.errorRadius = accuracy;
		this.timestamp = timestamp;
	}

	public LocationData(double latitude, double longitude, double altitude,
			float accuracy, Date timestamp) {
		this(latitude, longitude, altitude, accuracy, timestamp.getTime());
	}

	public int getFixTime() {
		return fixTime;
	}

	/**
	 * The time in seconds required to obtain a suitable location fix
	 * 
	 * @param fixTime
	 */
	public void setFixTime(int fixTime) {
		this.fixTime = fixTime;
	}

	/**
	 * Returns true if the reading took longer than the maximum allowed time to
	 * obtain a suitable location fix. In other words, this location is the best
	 * that was available in the allowed time.
	 */
	public boolean getFixTimedOut() {
		return fixTimedOut;
	}

	public void setFixTimedOut(boolean fixTimedOut) {
		this.fixTimedOut = fixTimedOut;
	}

	/**
	 * The GUID of the thing at this location. Leaving this unset implies the
	 * current device.
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

	public String getPrettyName() {
		return prettyName;
	}

	public void setPrettyName(String prettyName) {
		this.prettyName = prettyName;
	}

	/**
	 * Returns true if this location is for a fixed location (like a building)
	 */
	public boolean getFixed() {
		return fixed;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
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

	public float getErrorRadius() {
		return errorRadius;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public static final Parcelable.Creator<LocationData> CREATOR = new Parcelable.Creator<LocationData>() {

		@Override
		public LocationData createFromParcel(Parcel in) {
			LocationData ld = new LocationData(in.readDouble(),
					in.readDouble(), in.readDouble(), in.readFloat(),
					in.readLong());
			ld.setFixTime(in.readInt());
			ld.setFixTimedOut(in.readInt() == 1);
			ld.setFixed(in.readInt() == 1);
			ld.setSubjGuid(in.readString());
			ld.setGuid(in.readString());
			ld.setPrettyName(in.readString());
			return ld;
		}

		@Override
		public LocationData[] newArray(int size) {
			return new LocationData[size];
		}

	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeDouble(latitude);
		out.writeDouble(longitude);
		out.writeDouble(altitude);
		out.writeFloat(errorRadius);
		out.writeLong(timestamp);
		out.writeInt(fixTime);
		out.writeInt(fixTimedOut ? 1 : 0);
		out.writeInt(fixed ? 1 : 0);
		out.writeString(subjGuid);
		out.writeString(guid);
		out.writeString(prettyName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		return 0;
	}

}

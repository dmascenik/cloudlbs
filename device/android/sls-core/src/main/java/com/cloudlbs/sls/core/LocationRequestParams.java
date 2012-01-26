package com.cloudlbs.sls.core;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author Dan Mascenik
 * 
 */
public final class LocationRequestParams implements Parcelable, Cloneable {

	/**
	 * How long beyond the {@link #maxWaitSeconds} before this request expires.
	 * Default is 15 minutes.
	 */
	public static final long REQUEST_EXPIRATION_SECONDS = 15 * 60;

	public static final float MAX_ERROR_DEFAULT = 2000.0f;
	public static final int MAX_WAIT_DEFAULT = 5;
	public static final int MAX_GPS_AGE_DEFAULT = 5;

	private int frequencySeconds = 0;
	private float maxErrorMeters = MAX_ERROR_DEFAULT;
	private int maxWaitSeconds = MAX_WAIT_DEFAULT;
	private int maxGpsAgeSeconds = MAX_GPS_AGE_DEFAULT;
	private long initialDelayMillis = 0;
	private long recurringDurationMillis = 0;
	private int recurrenceCount = 0;

	public LocationRequestParams() {
	}

	/**
	 * Convenience constructor setting an initial delay.
	 * 
	 * @param initialDelayMillis
	 */
	public LocationRequestParams(long initialDelayMillis) {
		this();
		this.initialDelayMillis = initialDelayMillis;
	}

	public int getFrequencySeconds() {
		return frequencySeconds;
	}

	/**
	 * The frequency of a recurring location reading in seconds. The next
	 * reading is not scheduled until the previous one has been fulfilled, so
	 * the actual frequency of readings will most likely be less than the
	 * provided value.
	 * 
	 * @param frequencySeconds
	 */
	public void setFrequencySeconds(int frequencySeconds) {
		if (frequencySeconds < 0) {
			throw new IllegalArgumentException(
					"Recurring reading frequency cannot be less than 0");
		}
		this.frequencySeconds = frequencySeconds;
	}

	public float getMaxErrorMeters() {
		return maxErrorMeters;
	}

	/**
	 * The maximum error radius allowed for a location reading to be considered
	 * acceptably accurate. This may not actually be achieved. Max error radius
	 * must be larger than 2m.
	 * 
	 * @see #setMaxWaitSeconds(int)
	 * 
	 * @param maxErrorMeters
	 */
	public void setMaxErrorMeters(float maxErrorMeters) {
		if (maxErrorMeters < 2.0f) {
			throw new IllegalArgumentException(
					"Maximum error radius must be larger than 2m");
		}
		this.maxErrorMeters = maxErrorMeters;
	}

	public int getMaxWaitSeconds() {
		return maxWaitSeconds;
	}

	/**
	 * The maximum number of seconds to wait for an acceptably accurate reading
	 * before returning whatever reading is available (with the "timedOut" flag
	 * set). Max wait must be less that 2min.
	 * 
	 * @see #setMaxErrorMeters(float)
	 * 
	 * @param maxWaitSeconds
	 */
	public void setMaxWaitSeconds(int maxWaitSeconds) {
		if (maxWaitSeconds < 0) {
			throw new IllegalArgumentException("Max wait cannot be negative");
		}
		if (maxWaitSeconds > 120) {
			throw new IllegalArgumentException(
					"Max wait cannot be greater than 2min");
		}
		this.maxWaitSeconds = maxWaitSeconds;
	}

	public int getMaxGpsAgeSeconds() {
		return maxGpsAgeSeconds;
	}

	/**
	 * The maximum age in seconds for which a location reading can be considered
	 * recent enough to be used. Max GPS age cannot be greater than the sum of
	 * the max wait and {@value #REQUEST_EXPIRATION_SECONDS}s
	 * 
	 * @param maxGpsAgeSeconds
	 */
	public void setMaxGpsAgeSeconds(int maxGpsAgeSeconds) {
		if (maxGpsAgeSeconds < 0) {
			throw new IllegalArgumentException("Max GPS age cannot be negative");
		}
		if (maxGpsAgeSeconds > maxWaitSeconds + REQUEST_EXPIRATION_SECONDS) {
			throw new IllegalArgumentException("Max GPS age cannot "
					+ "be greater than" + "the expiration threshold of "
					+ maxWaitSeconds + REQUEST_EXPIRATION_SECONDS + "s");
		}

		this.maxGpsAgeSeconds = maxGpsAgeSeconds;
	}

	public long getInitialDelayMillis() {
		return initialDelayMillis;
	}

	/**
	 * The delay in milliseconds before a location reading should be performed.
	 * It is not necessary to use this setting to allow for the GPS to warm up
	 * and make a fix. The service will automatically warm up the GPS in advance
	 * of your reading.
	 * 
	 * @param initialDelayMillis
	 */
	public void setInitialDelayMillis(long initialDelayMillis) {
		this.initialDelayMillis = initialDelayMillis;
	}

	public long getRecurringDurationMillis() {
		return recurringDurationMillis;
	}

	/**
	 * How long to perform recurring readings in milliseconds. If set to zero,
	 * recurring readings will be performed indefinitely.
	 * 
	 * @param recurringDurationMillis
	 */
	public void setRecurringDurationMillis(long recurringDurationMillis) {
		if (recurringDurationMillis < 0) {
			throw new IllegalArgumentException(
					"Recurrence duration cannot be negative");
		}
		this.recurringDurationMillis = recurringDurationMillis;
	}

	public int getRecurrenceCount() {
		return recurrenceCount;
	}

	/**
	 * The number of recurring readings to perform. Recurrence will stop when
	 * this count is reached, or when {@link #getRecurringDurationMillis()} has
	 * passed (if set). A recurrence count of zero means to permit recurrence
	 * indefinitely. Recurrence count is limited between 2 and 10.
	 * 
	 * @param recurrenceCount
	 */
	public void setRecurrenceCount(int recurrenceCount) {
		if (recurrenceCount != 0) {
			if (recurrenceCount < 2) {
				throw new IllegalArgumentException(
						"Recurrence count must be greater than 2");
			}
			if (recurrenceCount > 10) {
				throw new IllegalArgumentException(
						"Recurrence count must be less than or equal to 10");
			}
		}
		this.recurrenceCount = recurrenceCount;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public static final Parcelable.Creator<LocationRequestParams> CREATOR = new Parcelable.Creator<LocationRequestParams>() {

		@Override
		public LocationRequestParams createFromParcel(Parcel in) {
			LocationRequestParams params = new LocationRequestParams();
			params.setFrequencySeconds(in.readInt());
			params.setInitialDelayMillis(in.readLong());
			params.setMaxErrorMeters(in.readFloat());
			params.setMaxGpsAgeSeconds(in.readInt());
			params.setMaxWaitSeconds(in.readInt());
			params.setRecurringDurationMillis(in.readLong());
			params.setRecurrenceCount(in.readInt());
			return params;
		}

		@Override
		public LocationRequestParams[] newArray(int size) {
			return new LocationRequestParams[size];
		}

	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(getFrequencySeconds());
		out.writeLong(getInitialDelayMillis());
		out.writeFloat(getMaxErrorMeters());
		out.writeInt(getMaxGpsAgeSeconds());
		out.writeInt(getMaxWaitSeconds());
		out.writeLong(getRecurringDurationMillis());
		out.writeInt(getRecurrenceCount());
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

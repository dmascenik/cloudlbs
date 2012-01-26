package com.cloudlbs.sls.core;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author Dan Mascenik
 * 
 */
public final class AuthenticationStatus implements Parcelable {

	public static final int FAULT_NONE = -1;
	public static final int FAULT_NO_NETWORK = 0;
	public static final int FAULT_CREDENTIALS = 1;
	public static final int FAULT_SERVICE_DISABLED = 2;
	public static final int FAULT_SERVICE_FAILURE = 3;
	public static final int FAULT_USER_DISABLED = 4;
	public static final int FAULT_INVALID_API_KEY = 5;

	/**
	 * The app is disabled - may be a user setting for this device, or the API
	 * key may have been temporarily revoked.
	 */
	public static final int FAULT_APP_DISABLED = 6;

	/**
	 * The app does not use user authentication
	 */
	public static final int FAULT_INVALID_APP = 7;

	private Boolean isSuccessful = false;
	private int faultCode = FAULT_NONE;

	public Boolean getIsSuccessful() {
		return isSuccessful;
	}

	public void setIsSuccessful(Boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}

	public int getFaultCode() {
		return faultCode;
	}

	public void setFaultCode(int faultCode) {
		this.faultCode = faultCode;
	}

	public static final Parcelable.Creator<AuthenticationStatus> CREATOR = new Parcelable.Creator<AuthenticationStatus>() {

		@Override
		public AuthenticationStatus createFromParcel(Parcel in) {
			AuthenticationStatus auth = new AuthenticationStatus();
			auth.setIsSuccessful(in.readInt() == 1 ? true : false);
			auth.setFaultCode(in.readInt());
			return auth;
		}

		@Override
		public AuthenticationStatus[] newArray(int size) {
			return new AuthenticationStatus[size];
		}

	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(getIsSuccessful() ? 1 : 0);
		out.writeInt(faultCode);
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

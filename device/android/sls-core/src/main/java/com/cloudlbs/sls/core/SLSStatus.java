package com.cloudlbs.sls.core;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author Dan Mascenik
 * 
 */
public final class SLSStatus implements Parcelable {

	public static final int FAULT_NO_NETWORK = 0;
	public static final int FAULT_NO_PROCESSOR_DESIGNATION = 1;
	public static final int FAULT_NO_XMPP_CONNECTION = 2;
	public static final int FAULT_NO_XMPP_LOGIN = 3;
	public static final int FAULT_XMPP_ROSTER = 4;
	public static final int FAULT_PROCESSOR_OFFLINE = 5;
	public static final int FAULT_NO_XMPP_CHAT = 6;
	public static final int FAULT_NONE = 7;

	private int faultCode = FAULT_NONE;

	public SLSStatus(int faultCode) {
		this.faultCode = faultCode;
	}

	public int getFaultCode() {
		return faultCode;
	}

	public void setFaultCode(int faultCode) {
		this.faultCode = faultCode;
	}

	public static final Parcelable.Creator<SLSStatus> CREATOR = new Parcelable.Creator<SLSStatus>() {

		@Override
		public SLSStatus createFromParcel(Parcel in) {
			return new SLSStatus(in.readInt());
		}

		@Override
		public SLSStatus[] newArray(int size) {
			return new SLSStatus[size];
		}

	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel out, int flags) {
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

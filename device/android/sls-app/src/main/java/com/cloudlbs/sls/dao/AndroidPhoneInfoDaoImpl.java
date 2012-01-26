package com.cloudlbs.sls.dao;

import android.telephony.TelephonyManager;

/**
 * Uses the Android {@link TelephonyManager} to provide information about the
 * phone state.
 * 
 * @author Dan Mascenik
 * 
 */
public class AndroidPhoneInfoDaoImpl implements PhoneInfoDao {

	private TelephonyManager telephonyManager;

	public AndroidPhoneInfoDaoImpl(TelephonyManager telephonyManager) {
		this.telephonyManager = telephonyManager;
	}

	@Override
	public String getDeviceUniqueId() {
		return "ANDROID-" + telephonyManager.getDeviceId();
	}

}

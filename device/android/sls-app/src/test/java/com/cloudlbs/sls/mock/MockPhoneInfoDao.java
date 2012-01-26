package com.cloudlbs.sls.mock;

import com.cloudlbs.sls.dao.PhoneInfoDao;

public class MockPhoneInfoDao implements PhoneInfoDao {

	@Override
	public String getDeviceUniqueId() {
		return "ANDROID-12345";
	}

}

package com.cloudlbs.sls.xmpp;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cloudlbs.sls.dao.OutboundMessageDao;
import com.cloudlbs.sls.dao.PhoneInfoDao;
import com.cloudlbs.sls.dao.PreferencesDao;
import com.cloudlbs.sls.http.DeviceRegistrationRemoteService;
import com.cloudlbs.sls.utils.Assert;

/**
 * @author Dan Mascenik
 * 
 */
public class AndroidProcessorClient extends ProcessorClient {

	private ConnectivityManager connectivityManager;

	/**
	 * @param deviceConnectionRemoteService
	 * @param phoneInfoDao
	 * @param connectivityManager
	 * @param preferencesDao
	 * @param outboundMessageDao
	 */
	public AndroidProcessorClient(
			DeviceRegistrationRemoteService deviceConnectionRemoteService,
			PhoneInfoDao phoneInfoDao, ConnectivityManager connectivityManager,
			PreferencesDao preferencesDao, OutboundMessageDao outboundMessageDao) {
		super(preferencesDao, outboundMessageDao, phoneInfoDao,
				deviceConnectionRemoteService);
		Assert.notNull(connectivityManager);
		this.connectivityManager = connectivityManager;
	}

	@Override
	protected boolean isNetworkAvailable() {
		if (connectivityManager == null) {
			return false;
		}
		NetworkInfo net = connectivityManager.getActiveNetworkInfo();
		return (net != null && net.isConnected());
	}

}

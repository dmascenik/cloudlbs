package com.cloudlbs.sls.http;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.cloudlbs.sls.dao.AndroidPreferencesDaoImpl;
import com.cloudlbs.sls.dao.PreferencesDao;
import com.cloudlbs.sls.event.ActivationListener;
import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.ShutdownEvent;
import com.cloudlbs.sls.event.StartupEvent;
import com.cloudlbs.sls.utils.HttpUtils;
import com.cloudlbs.sls.utils.Logger;
import com.google.protobuf.Message;

/**
 * This remote service gets the hostname and port from the
 * {@link PreferencesDao}. HTTPS is used by default unless in emulator mode.
 * 
 * @author Dan Mascenik
 * 
 */
public abstract class BaseRemoteService<M extends Message> extends
		RestProtobufRemoteService<M> implements ActivationListener {

	private static final int HTTP_CONNECTION_TIMEOUT = 3;
	private static final int HTTP_SOCKET_TIMEOUT = 10;

	private PreferencesDao preferencesDao;
	private String resourceUrlStub;

	/**
	 * @param preferencesDao
	 */
	@SuppressWarnings("unchecked")
	public BaseRemoteService(PreferencesDao preferencesDao,
			String resourceUrlStub) {
		super(null, null, null, resourceUrlStub);
		this.preferencesDao = preferencesDao;
		this.resourceUrlStub = resourceUrlStub;

		Type[] typeArgs = ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments();
		messageClass = (Class<M>) typeArgs[0];
		if (messageClass == null) {
			throw new IllegalArgumentException(
					"Message class parameter must be provided");
		}

		/*
		 * This client will timeout after 3s without a connection, and after 10s
		 * without a complete response.
		 */
		httpClient = HttpUtils.getThreadSafeAnyCertSSLClient();
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params,
				HTTP_CONNECTION_TIMEOUT * 1000);
		HttpConnectionParams.setSoTimeout(params, HTTP_SOCKET_TIMEOUT * 1000);
		HttpConnectionParams.setTcpNoDelay(params, true);

		EventDispatcher.addListener(this);
	}

	@Override
	public void onShutdown(ShutdownEvent evt) {
		// does nothing
	}

	@Override
	public void onStartupEvent(StartupEvent evt) {
		String hostAndPort = getBaseUrlFromPreferences(preferencesDao);
		Logger.debug("Configuring remote service at " + hostAndPort
				+ resourceUrlStub);
		setBaseUrl(hostAndPort);
	}

	private String getBaseUrlFromPreferences(PreferencesDao preferencesDao) {
		String hostname = preferencesDao.getMasterHostname();
		int port = preferencesDao.getMasterPort();
		String protocol;
		if (preferencesDao.getUseHttps()) {
			protocol = "https://";
		} else {
			protocol = "http://";
		}
		if (preferencesDao.getEmulatorMode()) {
			hostname = AndroidPreferencesDaoImpl.EMULATOR_HOST;
			port = AndroidPreferencesDaoImpl.EMULATOR_PORT;
			protocol = "http://";
		}
		return protocol + hostname + ":" + port;
	}

}

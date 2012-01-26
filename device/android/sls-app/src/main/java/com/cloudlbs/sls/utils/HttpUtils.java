package com.cloudlbs.sls.utils;

import java.security.KeyStore;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

/**
 * @author Dan Mascenik
 * 
 */
public class HttpUtils {

	public static DefaultHttpClient getThreadSafeClient() {

		DefaultHttpClient client = new DefaultHttpClient();
		ClientConnectionManager mgr = client.getConnectionManager();
		HttpParams params = client.getParams();

		client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,
				mgr.getSchemeRegistry()), params);

		return client;
	}

	public static DefaultHttpClient getThreadSafeAnyCertSSLClient() {
		KeyStore trustStore;
		SSLSocketFactory sf;
		try {
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			sf = new AnyCertSSLSocketFactory(trustStore);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", sf, 443));

		ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
				registry);

		return new DefaultHttpClient(ccm, params);
	}

}

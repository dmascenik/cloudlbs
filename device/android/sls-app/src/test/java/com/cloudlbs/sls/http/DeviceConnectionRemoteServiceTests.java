package com.cloudlbs.sls.http;

import org.junit.Assert;
import org.junit.Test;

import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.mock.MockPreferencesDao;
import com.cloudlbs.sls.protocol.DeviceConnectionProto.DeviceConnectionMessage;

/**
 * These require a local instance of the accounts service, processor, and an
 * XMPP server to be running. The tests look for the processor at
 * http://localhost:9040/processor
 * 
 * @author Dan Mascenik
 * 
 */
public class DeviceConnectionRemoteServiceTests extends Assert {

	static {
		EventDispatcher.initialize();
	}

	@Test
	public void testGetDeviceConnectionInfo() throws Exception {
		DeviceRegistrationRemoteService svc = new DeviceRegistrationRemoteService(
				new MockPreferencesDao());

		DeviceConnectionMessage.Builder db = DeviceConnectionMessage
				.newBuilder();
		db.setDeviceUniqueId("ANDROID-1234567891");

		DeviceConnectionMessage dcm = svc.create(db.build());
		System.out.println(dcm.toString());
	}
}

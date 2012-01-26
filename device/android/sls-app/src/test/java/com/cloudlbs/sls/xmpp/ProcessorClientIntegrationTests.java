package com.cloudlbs.sls.xmpp;

import org.junit.Before;
import org.junit.Test;

import com.cloudlbs.sls.BaseEventDrivenTestCase;
import com.cloudlbs.sls.event.NetworkStatusEvent;
import com.cloudlbs.sls.http.DeviceRegistrationRemoteService;
import com.cloudlbs.sls.mock.MockOutboundMessageDao;
import com.cloudlbs.sls.mock.MockPhoneInfoDao;
import com.cloudlbs.sls.mock.MockPreferencesDao;
import com.cloudlbs.sls.protocol.DeviceConnectionProto.DeviceConnectionMessage;

/**
 * These tests all require a localhost XMPP server to be running with 3 users
 * configured - testuser1, testuser2 and testuser3, all having password
 * "password". The tests will be run as testuser1.
 * 
 * @author Dan Mascenik
 * 
 */
public class ProcessorClientIntegrationTests extends BaseEventDrivenTestCase {

	@Test
	public synchronized void testConnectivityManagement() throws Exception {
		TestingProcessorClient client = new TestingProcessorClient();
		client.onNetworkStatusChange(new NetworkStatusEvent(true));
		wait();
	}

	@Before
	public void before() {
		super.before();
		sysOutLogger(true, 0);
	}

	class TestingProcessorClient extends ProcessorClient {

		String currentProcessor;

		public TestingProcessorClient() {
			super(new MockPreferencesDao(), new MockOutboundMessageDao(),
					new MockPhoneInfoDao(), new DeviceRegistrationRemoteService(
							new MockPreferencesDao()));
			TestingProcessorClient.this.ignoreResource = true;
		}

		@Override
		protected DeviceConnectionMessage getConnectionDetails() {
			DeviceConnectionMessage.Builder b = DeviceConnectionMessage
					.newBuilder();
			b.setDeviceUniqueId("ANDROID-12345");
			if (currentProcessor == null
					|| "testuser3@localhost".equals(currentProcessor)) {
				b.setProcessorName("testuser2@localhost");
				currentProcessor = "testuser2@localhost";
			} else {
				b.setProcessorName("testuser3@localhost");
				currentProcessor = "testuser3@localhost";
			}
			b.setXmppUsername("testuser1");
			b.setXmppPassword("password");
			return b.build();
		}

		@Override
		protected boolean isNetworkAvailable() {
			return true;
		}

	}
}

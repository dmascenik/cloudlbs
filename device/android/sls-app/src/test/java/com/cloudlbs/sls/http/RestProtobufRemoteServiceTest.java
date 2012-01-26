package com.cloudlbs.sls.http;

import org.junit.Assert;
import org.junit.Test;

import com.cloudlbs.sls.protocol.DeviceConnectionProto.DeviceConnectionMessage;

/**
 * @author Dan Mascenik
 * 
 */
public class RestProtobufRemoteServiceTest extends Assert {

	@Test
	public void testExtractMessage() throws Exception {
		MyRemoteService svc = new MyRemoteService();
		DeviceConnectionMessage.Builder b = DeviceConnectionMessage
				.newBuilder();
		b.setDeviceUniqueId("abc");
		byte[] bytes = b.build().toByteArray();

		DeviceConnectionMessage dcm = svc.convert(bytes);
		assertEquals("abc", dcm.getDeviceUniqueId());

	}

	class MyRemoteService extends
			RestProtobufRemoteService<DeviceConnectionMessage> {

		public MyRemoteService() {
			super(null, DeviceConnectionMessage.class, null, null);
		}

		public DeviceConnectionMessage convert(byte[] bytes) throws Exception {
			return toMessage(bytes);
		}

	}

}

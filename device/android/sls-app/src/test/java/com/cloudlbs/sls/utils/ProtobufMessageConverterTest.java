package com.cloudlbs.sls.utils;

import org.junit.Assert;
import org.junit.Test;

import com.cloudlbs.sls.protocol.DeviceConnectionProto.DeviceConnectionMessage;

/**
 * @author Dan Mascenik
 * 
 */
public class ProtobufMessageConverterTest extends Assert {

	MessageConverter<DeviceConnectionMessage, DeviceConnection> dcmc = new ProtobufMessageConverter<DeviceConnectionMessage, DeviceConnection>() {
	};

	@Test
	public void testToMessage() {
		DeviceConnection dc = new DeviceConnection();
		dc.setDeviceUniqueId("abc");
		dc.setOtherThing("def");
		dc.setProcessorName("proc1");
		dc.setXmppHost("localhost");
		dc.setXmppPassword("foo");
		dc.setXmppPort(123);
		dc.setXmppUsername("me");

		DeviceConnectionMessage dcm = dcmc.toMessage(dc);
		assertNotNull(dcm);
		assertEquals(dc.getDeviceUniqueId(), dcm.getDeviceUniqueId());
		assertEquals(dc.getProcessorName(), dcm.getProcessorName());
		assertEquals(dc.getXmppHost(), dcm.getXmppHost());
		assertEquals(dc.getXmppPassword(), dcm.getXmppPassword());
		assertEquals(dc.getXmppUsername(), dcm.getXmppUsername());
		assertEquals(dc.getXmppPort(), dcm.getXmppPort());
	}

	@Test
	public void testToObject() {
		DeviceConnectionMessage.Builder dcmb = DeviceConnectionMessage
				.newBuilder();
		dcmb.setDeviceUniqueId("abc");
		dcmb.setProcessorName("proc1");
		dcmb.setXmppHost("localhost");
		dcmb.setXmppPassword("foo");
		dcmb.setXmppPort(123);
		dcmb.setXmppUsername("me");

		DeviceConnectionMessage dcm = dcmb.build();
		DeviceConnection dc = dcmc.fromMessage(dcm);
		assertNotNull(dc);
		assertNull(dc.getOtherThing());
		assertEquals(dc.getDeviceUniqueId(), dcm.getDeviceUniqueId());
		assertEquals(dc.getProcessorName(), dcm.getProcessorName());
		assertEquals(dc.getXmppHost(), dcm.getXmppHost());
		assertEquals(dc.getXmppPassword(), dcm.getXmppPassword());
		assertEquals(dc.getXmppUsername(), dcm.getXmppUsername());
		assertEquals(dc.getXmppPort(), dcm.getXmppPort());
	}

}

package com.cloudlbs.core.utils.remote;

import junit.framework.TestCase;

import org.junit.Test;

public class ProtobufHttpMessageConverterTest extends TestCase {

	@Test
	public void testInitialize() throws Exception {
		new ProtobufHttpMessageConverter("com.cloudlbs.platform.protocol");
	}
}

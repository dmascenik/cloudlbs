package com.cloudlbs.sls.xmpp;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Dan Mascenik
 * 
 */
public class ResourceNameTest extends Assert {

	@Test
	public void testExtractUsernameAndResource() {
		String raw = "processor1@dev.cloud-lbs.com/processor1-jclust01";
		String username = raw.substring(0, raw.indexOf("/"));
		String processorName = "processor1@dev.cloud-lbs.com";
		String processorNickName = processorName.substring(0,
				processorName.indexOf("@"));

		String resourceName = raw.substring(raw.indexOf("/") + 1);

		assertEquals(processorName, username);
		assertEquals("processor1", processorNickName);
		assertEquals("processor1-jclust01", resourceName);

	}

}

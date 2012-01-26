package com.cloudlbs.core.utils.protocol;

import org.junit.Assert;
import org.junit.Test;

import com.cloudlbs.core.utils.test.ItemProto.ItemMessage;

/**
 * @author Dan Mascenik
 * 
 */
public class NoopMessageConverterTest extends Assert {

	NoopMessageConverter<ItemMessage> converter = new NoopMessageConverter<ItemMessage>(
			ItemMessage.class);

	@Test
	public void testFromAndToMessage() {
		ItemMessage.Builder b = ItemMessage.newBuilder();
		b.addLabel("label");
		b.setReqString("abc");
		ItemMessage item = b.build();

		assertTrue(item == converter.fromMessage(item));
		assertTrue(item == converter.toMessage(item));
	}

}

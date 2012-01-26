package com.cloudlbs.core.utils.test;

import com.cloudlbs.core.utils.protocol.ProtobufMessageConverter;
import com.cloudlbs.core.utils.test.ItemProto.ItemMessage;
import com.cloudlbs.core.utils.test.ThingProto.ThingMessage;

/**
 * @author Dan Mascenik
 * 
 */
public class ItemMessageConverter extends
		ProtobufMessageConverter<ItemMessage, Item> {

	public ItemMessageConverter() {

		// Just creating the anonymous type with the right generic params should
		// be enough
		new ProtobufMessageConverter<ThingMessage, Thing>() {
		};

	}

}

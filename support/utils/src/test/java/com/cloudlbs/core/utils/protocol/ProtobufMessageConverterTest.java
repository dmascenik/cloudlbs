package com.cloudlbs.core.utils.protocol;

import java.util.Date;
import java.util.Set;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.cloudlbs.core.utils.test.Item;
import com.cloudlbs.core.utils.test.ItemMessageConverter;
import com.cloudlbs.core.utils.test.ItemProto.ItemMessage;
import com.cloudlbs.core.utils.test.Thing;
import com.cloudlbs.core.utils.test.ThingProto.ThingMessage;

/**
 * @author Dan Mascenik
 * 
 */
public class ProtobufMessageConverterTest extends Assert {

	ItemMessageConverter converter = new ItemMessageConverter();

	@Test
	public void testFromMessageNoNestedMessages() {
		ItemMessage.Builder b = ItemMessage.newBuilder();
		b.setGuid("def");
		b.setReqString("test");
		b.setVersion(1234);
		b.setModDate(12345);
		b.setStrDate("2010-09-01T12:12:32.000Z");
		b.setProtoOnlyString("abc");
		ItemMessage m = b.build();

		Item item = converter.fromMessage(m);
		assertEquals(new Long(1234), item.getVersion());
		assertEquals("def", item.getGuid());

		// This one's a default
		assertEquals(new Integer(1), item.getAnInt());

		assertEquals(12345, item.getModDate().getTime());
		assertNotNull(item.getStrDate());
	}

	@Test
	public void testFromMessageWithSpecialDateString() {
		ItemMessage.Builder b = ItemMessage.newBuilder();
		b.setReqString("test");
		b.setStrDate("NOW/DAY-7DAYS"); // a week ago
		ItemMessage m = b.build();

		Item item = converter.fromMessage(m);

		assertNotNull(item.getStrDate());
	}

	@Test
	public void testFromMessageWithNestedMessage() {
		ItemMessage.Builder ib = ItemMessage.newBuilder();
		ThingMessage.Builder tb = ThingMessage.newBuilder();
		tb.setGuid("thing");
		tb.setModDate(new Date().getTime());
		ib.setThing(tb);
		ib.setReqString("test");
		ItemMessage m = ib.build();

		Item item = converter.fromMessage(m);
		assertNotNull(item.getThing());
		assertEquals("thing", item.getThing().getGuid());
		assertNotNull(item.getThing().getModDate());
	}

	@Test
	public void testFromMessageWithNestedMessageOfSameType() {
		ItemMessage.Builder parent = ItemMessage.newBuilder();
		ItemMessage.Builder child = ItemMessage.newBuilder();
		parent.setReqString("parent");
		child.setReqString("child");
		child.setParent(parent);

		/*
		 * Protobuf doesn't support cyclic references, but someday it may
		 */
		// parent.setParent(child);

		ItemMessage m = child.build();

		Item c = converter.fromMessage(m);
		assertEquals("child", c.getReqString());
		assertNotNull(c.getParent());
		assertEquals("parent", c.getParent().getReqString());
	}

	@Test
	public void testFromMessageWithRepeatedField() {
		ItemMessage.Builder b = ItemMessage.newBuilder();
		b.setReqString("abc");
		b.addLabel("label1");
		b.addLabel("label2");

		ItemMessage.Builder child = ItemMessage.newBuilder();
		child.setReqString("child");

		b.addChild(child);
		ItemMessage m = b.build();

		Item item = converter.fromMessage(m);
		assertEquals(2, item.getLabels().size());
		assertEquals(1, item.getChilds().size());
	}

	@Test
	public void testToMessageWithMinimumFields() {
		Item item = new Item();
		item.setReqString("str");

		ItemMessage m = converter.toMessage(item);
		assertNotNull(m);
		assertNotNull(m.getCreateDate());
		assertEquals("str", m.getReqString());
		assertFalse(m.hasParent());
	}

	@Test
	public void testToMessageWithNestedClass() {
		Item item = new Item();
		item.setReqString("str");

		Thing thing = new Thing();
		item.setThing(thing);

		Item item2 = new Item();
		item2.setReqString("abc");

		item.setParent(item2);

		ItemMessage m = converter.toMessage(item);
		assertNotNull(m);
		assertTrue(m.hasParent());
		assertEquals("abc", m.getParent().getReqString());
	}

	@Test
	public void testToMessageWithRepeatedNestedClasses() {
		Item item = new Item();
		item.setReqString("str");
		Set<Item> kids = item.getChilds();

		Item item2 = new Item();
		item2.setReqString("abc");

		kids.add(item2);

		ItemMessage m = converter.toMessage(item);
		assertNotNull(m);
		assertEquals(1, m.getChildCount());
	}

	@Test
	public void testFromBytes() {
		ItemMessage.Builder ib = ItemMessage.newBuilder();
		ib.setReqString("abc");
		ib.setAnInt(123);
		byte[] bytes = ib.build().toByteArray();

		Item item = (Item) ProtobufMessageConverter.fromMessageBytes(bytes,
				ItemMessage.class.getName());
		assertNotNull(item);
	}
	
	@Test
	public void testUnsetRepeatedField() {
		ItemMessage.Builder ib = ItemMessage.newBuilder();
		ib.setReqString("abc");
		ib.setAnInt(123);
		ItemMessage item = ib.build();
		
		List<String> unsetFields = converter.getUnsetMessageFields(item);
		assertTrue(unsetFields.contains("childs"));
		assertTrue(unsetFields.contains("labels"));
		assertTrue(unsetFields.contains("modDate"));
	}

}

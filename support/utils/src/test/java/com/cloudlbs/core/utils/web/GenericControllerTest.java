package com.cloudlbs.core.utils.web;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.junit.Test;

import com.cloudlbs.core.utils.search.Query;
import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.core.utils.service.GenericService;
import com.cloudlbs.core.utils.test.Item;
import com.cloudlbs.core.utils.test.ItemMessageConverter;
import com.cloudlbs.core.utils.test.ItemProto.ItemMessage;

public class GenericControllerTest extends
		JMockWebControllerTestCase<ItemMessage, Item> {

	@SuppressWarnings("unchecked")
	protected void before() {
		service = context.mock(GenericService.class);
		converter = new ItemMessageConverter();
		controller = new GenericController<ItemMessage, Item>(service,
				converter, ItemMessage.items) {

					@Override
					protected boolean isPreauthenticationAllowed() {
						return false;
					}
		};
	}

	@Test
	public void testPostProtobuf() throws Exception {

		final Item item = getMockItem();

		final ByteArrayServletOutputStream os = getServletOutputStream();

		context.checking(new Expectations() {
			{
				one(request).getRequestURI();
				will(returnValue("/item/abc"));

				one(request).getInputStream();
				will(returnValue(getServletInputStream(item)));

				one(service).createEntity(with(any(Item.class)));
				will(returnValue(item));

				one(response).setContentLength(with(any(int.class)));
				one(response).setContentType("application/vnd.google.protobuf");

				one(response).getOutputStream();
				will(returnValue(os));
			}
		});

		controller.postResource(request, response);
		assertTrue("Nothing in OutputStream", os.toByteArray().length > 0);
	}

	@Test
	public void testPostJSON() throws Exception {
		final Item item = getMockItem();

		final ByteArrayServletOutputStream os = getServletOutputStream();

		context.checking(new Expectations() {
			{
				one(request).getRequestURI();
				will(returnValue("/item/abc.json"));

				one(request).getInputStream();
				will(returnValue(getServletInputStream(item)));

				one(service).createEntity(with(any(Item.class)));
				will(returnValue(item));

				one(response).setContentLength(with(any(int.class)));
				one(response).setContentType("text/json");

				one(response).getOutputStream();
				will(returnValue(os));
			}
		});

		controller.postResource(request, response);
		assertTrue("Nothing in OutputStream", os.toByteArray().length > 0);
	}

	@Test
	public void testPostXML() throws Exception {
		final Item item = getMockItem();

		final ByteArrayServletOutputStream os = getServletOutputStream();

		context.checking(new Expectations() {
			{
				one(request).getRequestURI();
				will(returnValue("/item/abc.xml"));

				one(request).getInputStream();
				will(returnValue(getServletInputStream(item)));

				one(service).createEntity(with(any(Item.class)));
				will(returnValue(item));

				one(response).setContentLength(with(any(int.class)));
				one(response).setContentType("text/xml");

				one(response).getOutputStream();
				will(returnValue(os));
			}
		});

		controller.postResource(request, response);
		assertTrue("Nothing in OutputStream", os.toByteArray().length > 0);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdate() throws Exception {

		final Item item = getMockItem();

		final ByteArrayServletOutputStream os = getServletOutputStream();

		context.checking(new Expectations() {
			{
				one(request).getInputStream();
				will(returnValue(getServletInputStream(item)));

				one(service).updateEntity(with(any(String.class)),
						with(any(Item.class)), with(any(List.class)));
				will(returnValue(item));

				one(response).setContentLength(with(any(int.class)));
				one(response).setContentType("application/vnd.google.protobuf");

				one(response).getOutputStream();
				will(returnValue(os));
			}
		});

		controller.putResource(item.getGuid(), request, response);
		assertTrue("Nothing in OutputStream", os.toByteArray().length > 0);
	}

	@Test
	public void testDelete() throws Exception {

		final Item item = getMockItem();

		final ByteArrayServletOutputStream os = getServletOutputStream();

		context.checking(new Expectations() {
			{
				one(request).getRequestURI();
				will(returnValue("/item/abc"));

				one(service).retrieveEntity(with(any(String.class)));
				will(returnValue(item));
				one(service).deleteEntity(with(any(String.class)));

				one(response).setContentLength(with(any(int.class)));
				one(response).setContentType("application/vnd.google.protobuf");

				one(response).getOutputStream();
				will(returnValue(os));
			}
		});

		controller.deleteResource(item.getGuid(), request, response);
		assertTrue("Nothing in OutputStream", os.toByteArray().length > 0);
	}

	@Test
	public void testPostQuery() throws Exception {

		Item item0 = new Item("abc");
		item0.setReqString("def");
		Item item1 = new Item("def");
		item1.setReqString("def");
		Item item2 = new Item("ghi");
		item2.setReqString("def");
		List<Item> results = new ArrayList<Item>();
		results.add(item0);
		results.add(item1);
		results.add(item2);

		Query q = new Query("", 100, 20);
		final SearchResult<Item> sr = new SearchResult<Item>(results, q, 100, 2);

		final ByteArrayServletOutputStream os = getServletOutputStream();

		context.checking(new Expectations() {
			{
				one(request).getRequestURI();
				will(returnValue("/item/query"));

				one(request).getInputStream();
				will(returnValue(getQueryServletInputStream()));

				one(service).search(with(any(Query.class)));
				will(returnValue(sr));

				one(response).setContentLength(with(any(int.class)));
				one(response).setContentType("application/vnd.google.protobuf");

				one(response).getOutputStream();
				will(returnValue(os));
			}
		});

		controller.postQuery(request, response);
		assertTrue("Nothing in OutputStream", os.toByteArray().length > 0);
	}

	@Test
	public void testGetQuery() throws Exception {

		Item item0 = new Item("abc");
		item0.setReqString("def");
		Item item1 = new Item("def");
		item1.setReqString("def");
		Item item2 = new Item("ghi");
		item2.setReqString("def");
		List<Item> results = new ArrayList<Item>();
		results.add(item0);
		results.add(item1);
		results.add(item2);

		Query q = new Query("", 100, 20);
		final SearchResult<Item> sr = new SearchResult<Item>(results, q, 100, 2);

		final ByteArrayServletOutputStream os = getServletOutputStream();

		context.checking(new Expectations() {
			{
				one(request).getRequestURI();
				will(returnValue("/item/query"));

				one(request).getParameter("q");
				will(returnValue(""));
				one(request).getParameter("count");
				will(returnValue(null));
				one(request).getParameter("startIndex");
				will(returnValue(null));
				one(request).getParameter("scope");
				will(returnValue(null));

				one(service).search(with(any(Query.class)));
				will(returnValue(sr));

				one(response).setContentLength(with(any(int.class)));
				one(response).setContentType("application/vnd.google.protobuf");

				one(response).getOutputStream();
				will(returnValue(os));
			}
		});

		controller.getQuery(request, response);
		assertTrue("Nothing in OutputStream", os.toByteArray().length > 0);
	}

	private Item getMockItem() {
		Item item = new Item("abc");
		item.setReqString("def");
		return item;
	}
}

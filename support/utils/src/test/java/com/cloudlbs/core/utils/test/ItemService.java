package com.cloudlbs.core.utils.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.cloudlbs.core.utils.search.Query;
import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.core.utils.service.GenericService;

/**
 * @author Dan Mascenik
 * 
 */
public class ItemService implements GenericService<Item> {

	@Override
	public SearchResult<Item> search(Query query) {
		Item item = new Item("abc");
		item.setReqString("def");
		List<Item> values = new ArrayList<Item>();
		values.add(item);
		return new SearchResult<Item>(values, new Query("", 0, 10), 0, 1);
	}

	@Override
	public long count(Query query) {
		return 1;
	}

	@Override
	public Item createEntity(Item representation) {
		Item item = new Item("abc");
		item.setReqString("def");
		return item;
	}

	@Override
	public void deleteEntity(String id) {
	}

	@Override
	public Item retrieveEntity(String id) {
		Assert.isTrue("abc".equals(id),
				"Only retrieves entity for guid \"abc\" but got " + id);
		Item item = new Item("abc");
		item.setReqString("def");
		return item;
	}

	@Override
	public Item updateEntity(String id, Item representation,
			List<String> unmodifiedFields) {
		Item item = new Item("abc");
		item.setReqString("def");
		return item;
	}

	@Override
	public Class<Item> entityClass() {
		return Item.class;
	}

}

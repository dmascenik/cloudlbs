package com.cloudlbs.core.utils.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloudlbs.core.utils.service.GenericService;
import com.cloudlbs.core.utils.test.ItemProto.ItemMessage;
import com.cloudlbs.core.utils.web.GenericController;

/**
 * @author Dan Mascenik
 * 
 */
@Controller
@RequestMapping("/item")
public class ItemController extends GenericController<ItemMessage, Item> {

	@Autowired
	public ItemController(GenericService<Item> itemService) {
		super(itemService, new ItemMessageConverter(), ItemMessage.items);
	}

	@Override
	protected boolean isPreauthenticationAllowed() {
		return false;
	}
}

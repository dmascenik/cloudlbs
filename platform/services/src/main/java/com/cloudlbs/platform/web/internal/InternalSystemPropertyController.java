package com.cloudlbs.platform.web.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloudlbs.core.utils.web.GenericController;
import com.cloudlbs.platform.core.InternalGenericService;
import com.cloudlbs.platform.domain.SystemProperty;
import com.cloudlbs.platform.protocol.SystemProto.SystemPropertyMessage;

/**
 * Internal-only controller
 * 
 */
@Controller
@RequestMapping("/internal/sysprop")
public class InternalSystemPropertyController extends
		GenericController<SystemPropertyMessage, SystemProperty> {

	@Autowired
	public InternalSystemPropertyController(
			InternalGenericService<SystemPropertyMessage, SystemProperty> systemPropertyService) {
		super(systemPropertyService, systemPropertyService
				.getMessageConverter(), systemPropertyService
				.getMessageTypeExtension());
	}

	@Override
	protected boolean isPreauthenticationAllowed() {
		return true;
	}
	
}

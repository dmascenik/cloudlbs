package com.cloudlbs.platform.web.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloudlbs.core.utils.web.GenericController;
import com.cloudlbs.platform.core.InternalGenericService;
import com.cloudlbs.platform.domain.App;
import com.cloudlbs.platform.protocol.AppProto.AppMessage;

/**
 * Internal-only controller
 * 
 */
@Controller
@RequestMapping("/internal/app")
public class InternalAppController extends GenericController<AppMessage, App> {

	@Autowired
	public InternalAppController(
			InternalGenericService<AppMessage, App> appService) {
		super(appService, appService.getMessageConverter(), appService
				.getMessageTypeExtension());
	}

	@Override
	protected boolean isPreauthenticationAllowed() {
		return true;
	}
}

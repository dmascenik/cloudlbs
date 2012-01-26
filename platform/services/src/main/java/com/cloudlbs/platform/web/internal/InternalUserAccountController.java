package com.cloudlbs.platform.web.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloudlbs.core.utils.web.GenericController;
import com.cloudlbs.platform.core.InternalGenericService;
import com.cloudlbs.platform.domain.UserAccount;
import com.cloudlbs.platform.protocol.UserAccountProto.UserAccountMessage;

/**
 * Internal-only controller
 * 
 */
@Controller
@RequestMapping("/internal/uacct")
public class InternalUserAccountController extends
		GenericController<UserAccountMessage, UserAccount> {

	@Autowired
	public InternalUserAccountController(
			InternalGenericService<UserAccountMessage, UserAccount> userAccountService) {
		super(userAccountService, userAccountService.getMessageConverter(),
				userAccountService.getMessageTypeExtension());
	}

	@Override
	protected boolean isPreauthenticationAllowed() {
		return true;
	}
}

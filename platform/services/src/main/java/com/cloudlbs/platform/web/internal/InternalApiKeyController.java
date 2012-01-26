package com.cloudlbs.platform.web.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloudlbs.core.utils.web.GenericController;
import com.cloudlbs.platform.core.InternalGenericService;
import com.cloudlbs.platform.domain.ApiKey;
import com.cloudlbs.platform.protocol.ApiKeyProto.ApiKeyMessage;

/**
 * Internal-only controller
 * 
 */
@Controller
@RequestMapping("/internal/apikey")
public class InternalApiKeyController extends
		GenericController<ApiKeyMessage, ApiKey> {

	@Autowired
	public InternalApiKeyController(
			InternalGenericService<ApiKeyMessage, ApiKey> apiKeyService) {
		super(apiKeyService, apiKeyService.getMessageConverter(), apiKeyService
				.getMessageTypeExtension());
	}

	@Override
	protected boolean isPreauthenticationAllowed() {
		return true;
	}
}

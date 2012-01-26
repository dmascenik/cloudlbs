package com.cloudlbs.platform.service.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudlbs.core.utils.dao.JpaGenericDao;
import com.cloudlbs.core.utils.protocol.MessageConverter;
import com.cloudlbs.core.utils.remote.SecureRestTemplate;
import com.cloudlbs.platform.core.LocalOrRemoteService;
import com.cloudlbs.platform.domain.ApiKey;
import com.cloudlbs.platform.protocol.ApiKeyProto.ApiKeyMessage;

@Service
public class ApiKeyService extends
		LocalOrRemoteService<ApiKeyMessage, ApiKey, Long> {

	public static final String SERVICE_NAME_KEY = "apikey";

	@Autowired
	public ApiKeyService(JpaGenericDao<ApiKey, Long> apiKeyDao,
			SecureRestTemplate secureRestTemplate,
			MessageConverter<ApiKeyMessage, ApiKey> apiKeyMessageConverter,
			SystemPropertyService systemPropertyService) {
		super(apiKeyDao, secureRestTemplate, apiKeyMessageConverter,
				systemPropertyService, ApiKeyMessage.items);
	}

	@Override
	public String getServiceNameSysPropKey() {
		return SERVICE_NAME_KEY;
	}

}

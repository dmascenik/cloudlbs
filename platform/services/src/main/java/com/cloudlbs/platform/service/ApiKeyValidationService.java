package com.cloudlbs.platform.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudlbs.core.utils.search.Query;
import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.core.utils.service.GenericService;
import com.cloudlbs.platform.core.ApiKeyUtils;
import com.cloudlbs.platform.domain.ApiKey;
import com.cloudlbs.platform.service.internal.DeviceService;
import com.cloudlbs.sls.protocol.AppDetailsProto.AppDetailsMessage;
import com.cloudlbs.sls.protocol.AppDetailsProto.AppDetailsMessage.Builder;

/**
 * @author Dan Mascenik
 * 
 */
@Service
public class ApiKeyValidationService implements
		GenericService<AppDetailsMessage> {

	@Autowired
	private GenericService<ApiKey> apiKeyService;

	@Autowired
	private GenericService<DeviceService> deviceService;
	
	@Override
	public AppDetailsMessage createEntity(AppDetailsMessage adm) {
		String keyString = adm.getApiKey();
		String appIdentifier = adm.getAppIdentifier();
		String certHex = adm.getCertificateFingerprint();

		Builder resultBuilder = AppDetailsMessage.newBuilder(adm);
		resultBuilder.setIsValid(false);

		SearchResult<ApiKey> result = apiKeyService.search(new Query(
				"keyString: " + keyString, 0, 1));
		if (result.getTotalResults() == 1) {
			ApiKey apiKey = result.getValues().get(0);
			if (ApiKeyUtils.isValid(keyString, appIdentifier, certHex,
					apiKey.getSeed())
					&& apiKey.getExpirationDate().getTime() > System
							.currentTimeMillis()) {
				// API key is valid and is not expired
				resultBuilder.setIsValid(true);
				resultBuilder.setApiKeyExpirationDate(apiKey
						.getExpirationDate().getTime());
				resultBuilder.setAppGuid(apiKey.getApp().getGuid());
				resultBuilder.setAppName(apiKey.getApp().getName());
				resultBuilder.setUsesUserAccounts(apiKey.getApp()
						.getUsesUserAccounts());
				
				// TODO associate the app with the device
			}
		}

		return resultBuilder.build();
	}

	@Override
	public AppDetailsMessage retrieveEntity(String arg0)
			throws EntityNotFoundException {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public SearchResult<AppDetailsMessage> search(Query query) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public long count(Query query) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public void deleteEntity(String id) throws EntityNotFoundException {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public AppDetailsMessage updateEntity(String id,
			AppDetailsMessage representation, List<String> unmodifiedFields)
			throws EntityNotFoundException {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public Class<AppDetailsMessage> entityClass() {
		return AppDetailsMessage.class;
	}
}

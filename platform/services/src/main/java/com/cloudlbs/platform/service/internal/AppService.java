package com.cloudlbs.platform.service.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudlbs.core.utils.dao.JpaGenericDao;
import com.cloudlbs.core.utils.protocol.MessageConverter;
import com.cloudlbs.core.utils.remote.SecureRestTemplate;
import com.cloudlbs.platform.core.LocalOrRemoteService;
import com.cloudlbs.platform.domain.App;
import com.cloudlbs.platform.protocol.AppProto.AppMessage;

@Service
public class AppService extends LocalOrRemoteService<AppMessage, App, Long> {

	public static final String SERVICE_NAME_KEY = "app";

	@Autowired
	public AppService(JpaGenericDao<App, Long> appDao,
			SecureRestTemplate secureRestTemplate,
			MessageConverter<AppMessage, App> appMessageConverter,
			SystemPropertyService systemPropertyService) {
		super(appDao, secureRestTemplate, appMessageConverter,
				systemPropertyService, AppMessage.items);
	}

	@Override
	public String getServiceNameSysPropKey() {
		return SERVICE_NAME_KEY;
	}

}

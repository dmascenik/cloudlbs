package com.cloudlbs.platform.service.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudlbs.core.utils.dao.JpaGenericDao;
import com.cloudlbs.core.utils.protocol.MessageConverter;
import com.cloudlbs.core.utils.remote.SecureRestTemplate;
import com.cloudlbs.platform.core.LocalOrRemoteService;
import com.cloudlbs.platform.domain.UserAccount;
import com.cloudlbs.platform.protocol.UserAccountProto.UserAccountMessage;

@Service("userAccountService")
public class UserAccountService extends
		LocalOrRemoteService<UserAccountMessage, UserAccount, Long> {

	public static final String SERVICE_NAME_KEY = "useraccounts";

	@Autowired
	public UserAccountService(
			JpaGenericDao<UserAccount, Long> userAccountDao,
			SystemPropertyService systemPropertyService,
			MessageConverter<UserAccountMessage, UserAccount> userAccountMessageConverter,
			SecureRestTemplate secureRestTemplate) {
		super(userAccountDao, secureRestTemplate, userAccountMessageConverter,
				systemPropertyService, UserAccountMessage.items);
	}

	@Override
	public String getServiceNameSysPropKey() {
		return SERVICE_NAME_KEY;
	}

}

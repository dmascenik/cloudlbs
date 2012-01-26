package com.cloudlbs.web.server.remote;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cloudlbs.core.utils.Initializable;
import com.cloudlbs.core.utils.remote.RemoteService;
import com.cloudlbs.core.utils.system.SystemPropertyKeys;
import com.cloudlbs.core.utils.system.SystemPropertyKeys.Categories;
import com.cloudlbs.core.utils.system.SystemPropertyRemoteService;
import com.cloudlbs.platform.protocol.UserAccountProto.UserAccountMessage;
import com.cloudlbs.web.shared.dto.UserAccountDTO;

/**
 * @author Dan Mascenik
 * 
 */
public class RemoteServiceInitializer implements Initializable {

	@Autowired
	private RemoteService<UserAccountMessage, UserAccountDTO> userAccountRemote;

	@Autowired
	private RemoteService<UserAccountMessage, UserAccountMessage> restUserAccountDetailsService;

	@Autowired
	private SystemPropertyRemoteService systemPropertyService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cloudlbs.core.utils.Initializable#initialize()
	 */
	public void initialize() throws Exception {
		Properties sysProps = systemPropertyService
				.findByCategory(Categories.SYSTEM_SERVICES);
		String accountsUrl = sysProps
				.getProperty(SystemPropertyKeys.SERVICE_ACCOUNTS_URL);
		String userStub = sysProps
				.getProperty(SystemPropertyKeys.RESOURCE_USER_INTERNAL_STUB);

		userAccountRemote.setServiceUrl(accountsUrl);
		userAccountRemote.setResourceStub(userStub);
		log.debug("Initializing user account service at: " + accountsUrl
				+ userStub);

		restUserAccountDetailsService.setServiceUrl(accountsUrl);
		restUserAccountDetailsService.setResourceStub(userStub);
		log.debug("Initializing user detail service at: " + accountsUrl
				+ userStub);

	}

	private Logger log = LoggerFactory.getLogger(getClass());

}

package com.cloudlbs.platform.initializer;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.cloudlbs.core.utils.Initializable;
import com.cloudlbs.core.utils.StringUtils;
import com.cloudlbs.core.utils.remote.RestProtobufRemoteService;
import com.cloudlbs.platform.core.RemoteableService;
import com.cloudlbs.platform.core.SystemPropertyConstants;
import com.cloudlbs.platform.domain.SystemProperty;
import com.cloudlbs.platform.service.internal.SystemPropertyService;

/**
 * The {@link SystemPropertyService} can be switched to use only remote REST API
 * calls rather than attempting to access a database (which may not be present
 * on some nodes). This is possible by setting the
 * {@value SystemPropertyConstants#NO_DATABASE_PROPERTY} system property to true
 * with a <code>-D</code> flag on startup.
 * 
 * @author Dan Mascenik
 * 
 */
public class ServiceInitializer implements Initializable {

	private Logger log = LoggerFactory.getLogger(getClass());

	private boolean initialized = false;

	@Autowired
	private SystemPropertyService spService;

	@Autowired
	private RemoteableService<?, ?> systemPropertyService;

	@Autowired
	private RemoteableService<?, ?> userAccountService;

	@Autowired
	private RemoteableService<?, ?> deviceService;

	@Autowired
	private RemoteableService<?, ?> apiKeyService;

	@Autowired
	private RemoteableService<?, ?> appService;

	@Transactional
	public synchronized void initialize() {
		if (!initialized) {

			/*
			 * Self reference ensures that the service doesn't fail-back to
			 * local access.
			 */
			systemPropertyService.setSystemPropertyService(spService);

			String noDBStr = System
					.getProperty(SystemPropertyConstants.NO_DATABASE_PROPERTY);
			boolean noDB = Boolean.valueOf(noDBStr);
			if (noDB) {
				log.info("No database available. Setting ALL services to use REST API");

				/*
				 * Set this for each RemoteableService
				 */
				systemPropertyService.setUseRemote(noDB);
				userAccountService.setUseRemote(noDB);
				deviceService.setUseRemote(noDB);
				appService.setUseRemote(noDB);
				apiKeyService.setUseRemote(noDB);

				/*
				 * No database, so hopefully system properties were set for
				 * accessing the system properties service via the REST API or
				 * we're out of luck.
				 */
				String syspropUrl = System
						.getProperty(SystemPropertyConstants.SYSTEM_PROPERTY_SERVICE_URL);
				String syspropStub = System
						.getProperty(SystemPropertyConstants.SYSTEM_PROPERTY_RESOURCE_STUB);
				if (StringUtils.isBlank(syspropUrl)
						|| StringUtils.isBlank(syspropStub)) {
					throw new RuntimeException(
							SystemPropertyConstants.NO_DATABASE_PROPERTY
									+ " property set but "
									+ SystemPropertyConstants.SYSTEM_PROPERTY_SERVICE_URL
									+ " and "
									+ SystemPropertyConstants.SYSTEM_PROPERTY_RESOURCE_STUB
									+ " not both set. Cannot continue initialization.");
				}

				RestProtobufRemoteService<?, ?> systemPropertyRemoteService = systemPropertyService
						.getRemoteService();
				systemPropertyRemoteService.setServiceUrl(syspropUrl);
				systemPropertyRemoteService.setResourceStub(syspropStub);
				log.info("Initializing remote system property service at: "
						+ syspropUrl + syspropStub);

			}

			Properties sysProps = spService
					.getAsProperties(SystemProperty.CATEGORY_SERVICES);
			Assert.notEmpty(sysProps.keySet(), "No system properties found!");

			if (!noDB) {
				/*
				 * SystemPropertyRemoteService when a DB is available
				 */
				initializeService(sysProps,
						systemPropertyService.getServiceNameSysPropKey(),
						systemPropertyService.getRemoteService());
			}

			/*
			 * UserAccountRemoteService
			 */
			initializeService(sysProps,
					userAccountService.getServiceNameSysPropKey(),
					userAccountService.getRemoteService());

			/*
			 * DeviceRemoteService
			 */
			initializeService(sysProps,
					deviceService.getServiceNameSysPropKey(),
					deviceService.getRemoteService());

			/*
			 * ApiKeyService
			 */
			initializeService(sysProps,
					apiKeyService.getServiceNameSysPropKey(),
					apiKeyService.getRemoteService());

			/*
			 * AppService
			 */
			initializeService(sysProps, appService.getServiceNameSysPropKey(),
					appService.getRemoteService());

			initialized = true;
		}
	}

	private void initializeService(Properties sysProps, String serviceKey,
			RestProtobufRemoteService<?, ?> remoteService) {
		String url = (String) sysProps.get(SystemProperty.SERVICE_KEY_PREFIX
				+ serviceKey + SystemProperty.SERVICE_REMOTE_BASEURL_SUFFIX);
		String stub = (String) sysProps.get(SystemProperty.SERVICE_KEY_PREFIX
				+ serviceKey
				+ SystemProperty.SERVICE_REMOTE_RESOURCESTUB_SUFFIX);
		remoteService.setServiceUrl(url);
		remoteService.setResourceStub(stub);
		log.info("Initializing " + serviceKey + " service at: " + url + stub);

	}
}
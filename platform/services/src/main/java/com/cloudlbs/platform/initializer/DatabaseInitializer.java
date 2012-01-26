package com.cloudlbs.platform.initializer;

import java.util.List;
import java.util.UUID;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.cloudlbs.core.utils.Initializable;
import com.cloudlbs.core.utils.StringUtils;
import com.cloudlbs.core.utils.dao.JpaGenericDao;
import com.cloudlbs.platform.core.ApiKeyUtils;
import com.cloudlbs.platform.core.SystemPropertyConstants;
import com.cloudlbs.platform.domain.ApiKey;
import com.cloudlbs.platform.domain.App;
import com.cloudlbs.platform.domain.Role;
import com.cloudlbs.platform.domain.SystemProperty;
import com.cloudlbs.platform.domain.UserAccount;
import com.cloudlbs.platform.service.internal.ApiKeyService;
import com.cloudlbs.platform.service.internal.AppService;
import com.cloudlbs.platform.service.internal.DeviceService;
import com.cloudlbs.platform.service.internal.SystemPropertyServiceImpl;
import com.cloudlbs.platform.service.internal.UserAccountService;

/**
 * On server startup, checks if the schema has been populated with the basic
 * dataset (by checking for the existence of the "admin" user account).
 * 
 * @author Dan Mascenik
 * 
 */
public class DatabaseInitializer implements Initializable {

	/*
	 * STRONGLY recommend updating these keys after initial production install
	 */
	private static String privateKey = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwg"
			+ "gE8AgEAAkEAtHuiyKzOJwXW4+Zo/S6SGihNC0ppkZW/K42GRRRCuOBjZswZ4Ay"
			+ "W/T2ixC6zbCruGDQSOvTG4VaSDhLil5YbkQIDAQABAkEAqHN8y6/9+Y4Js0wqU"
			+ "aRV2PQkdJVPUwJhG1VMLM1sOQAfwYfk7UzmHfmAab/gMxRxwXLtzURIGLhhBwN"
			+ "pCpXpwQIhAPrB8fNKRQ1zJMahx8w2YCuYuOuniBBxpMmj8ePLadMvAiEAuEGUr"
			+ "tVR3mc1/t/oCIueizqOcG907TmE8Zl0B4LaTT8CIQCkpx8RKm5nI3k9eFNyMy4"
			+ "40kJycoI0kfqrcJxPgGzPfwIgZ6Ga+mpITYpHOD6+xm+gkDYy/tHxNatwNmJUf"
			+ "BUAqwkCIQD3EgSE420NdG4KTpxmciMlOB3vgJ8hSe4siv5SIHmqKw==";
	private static String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALR7osi"
			+ "szicF1uPmaP0ukhooTQtKaZGVvyuNhkUUQrjgY2bMGeAMlv09osQus2wq7hg0Ej"
			+ "r0xuFWkg4S4peWG5ECAwEAAQ==";

	private static boolean verified = false;
	private int propertiesAdded = 0;

	private Logger log = LoggerFactory.getLogger(getClass());

	@Transactional
	public synchronized void initialize() {
		if (!verified) {
			String noDBStr = System
					.getProperty(SystemPropertyConstants.NO_DATABASE_PROPERTY);
			boolean noDB = Boolean.valueOf(noDBStr);
			if (noDB) {
				log.info(SystemPropertyConstants.NO_DATABASE_PROPERTY
						+ " property set.");
				verified = true;
				return;
			}

			log.debug("Context loaded - verifying database...");

			Criterion adminGuid = Restrictions.eq("username", "admin");
			List<UserAccount> uaResult = userAccountDao
					.findByCriteria(adminGuid);
			boolean hasAdmin = (uaResult.size() == 1);

			if (!hasAdmin) {
				log.warn("Uninitialized database (no admin user). Initializing...");
				initializeDatabase();
				log.info("Database initialized successfully - "
						+ "admin login is now admin/password");
			} else {
				log.debug("Database already initialized - doing nothing");
			}

			/*
			 * WhereRU is useful to have in any environment
			 */
			Criterion whereRU = Restrictions.eq("name", "WhereRU?");
			List<App> appResult = appDao.findByCriteria(whereRU);
			boolean hasWhereRU = (appResult.size() == 1);
			String androidCert = System
					.getProperty(SystemPropertyConstants.CLOUDLBS_ANDROID_CERT_FINGERPRINT);

			if (!hasWhereRU && !StringUtils.isBlank(androidCert)) {
				log.info("Adding WhereRU? app...");
				App whereru = new App(UUID.randomUUID().toString());
				whereru.setAndroidPackageName("com.cloudlbs.whereru");
				whereru.setIosBundleId("com.cloudlbs.WhereRU");
				whereru.setName("WhereRU?");
				whereru.setUsesUserAccounts(true);
				appDao.makePersistent(whereru);

				String cleanCert = androidCert.replaceAll("\\W", "");
				String seed = "abcdef";
				String keyString = ApiKeyUtils.generateKeyString(
						whereru.getAndroidPackageName(), cleanCert, seed);

				ApiKey whereruKey = new ApiKey(UUID.randomUUID().toString());
				whereruKey.setApp(whereru);
				whereruKey.setCertFingerprint(cleanCert);
				whereruKey.setSeed(seed);
				whereruKey.setKeyString(keyString);
				whereruKey.setPlatform(ApiKey.PLATFORM_ANDROID);
				apiKeyDao.makePersistent(whereruKey);
				log.info("WhereRU? registered with API key: " + keyString);
			} else {
				log.info("WhereRU? app already present or -D"
						+ SystemPropertyConstants.CLOUDLBS_ANDROID_CERT_FINGERPRINT
						+ " not set - not generating API key");
			}

			/*
			 * Synchronizing system properties - new properties will be created
			 * with default values, existing properties will be untouched.
			 */
			synchronizeSystemProperties();
			verified = true;
		}
	}

	@Autowired
	JpaGenericDao<UserAccount, Long> userAccountDao;

	@Autowired
	JpaGenericDao<Role, Long> roleDao;

	@Autowired
	JpaGenericDao<SystemProperty, Long> systemPropertyDao;

	@Autowired
	JpaGenericDao<App, Long> appDao;

	@Autowired
	JpaGenericDao<ApiKey, Long> apiKeyDao;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Transactional
	public void initializeDatabase() {

		/*
		 * Create the admin role
		 */
		Role adminRole = new Role(UUID.randomUUID().toString(), "ROLE_ADMIN");
		roleDao.makePersistent(adminRole);

		/*
		 * Create the admin user
		 */
		UserAccount admin = new UserAccount(UUID.randomUUID().toString(),
				"admin");
		admin.setEmail("admin@cloudlbs.com");
		admin.setUsername("admin");
		admin.setFirstName("Root");
		admin.setLastName("Admin");
		admin.setStatus(UserAccount.STATUS_OK);
		// admin.setScopeGuid("ROOT");
		userAccountDao.makePersistent(admin);

		// Strongly recommend changing this after initial production install
		admin.setPassword(passwordEncoder.encodePassword("password",
				admin.getGuid()));
		admin.addRole(adminRole);

	}

	/*
	 * Adds system properties with default values if they don't exist.
	 */
	@Transactional
	private void synchronizeSystemProperties() {
		log.debug("Adding new system properties...");

		String cat = SystemProperty.CATEGORY_WEBAPP;
		addSysProp(cat, SystemProperty.KEY_BASE_URL_PUBLIC, "Public Base URL",
				"http://localhost:8000/cloudlbs",
				"The publicly visible base URL of the system.");

		addSysProp(cat, SystemProperty.KEY_REGISTER_CONFIRM_URLSTUB,
				"Account Confirmation URL", "/confirm",
				"What to append to the system base URL to get the URL of"
						+ " the account confirmation servlet");

		cat = SystemProperty.CATEGORY_MAIL;
		addSysProp(cat, SystemProperty.KEY_MAIL_HOST, "Mail Host",
				"mail.danmascenik.com",
				"The SMTP host to use when sending email to users");

		addSysProp(cat, SystemProperty.KEY_MAIL_NOREPLY_ADDRESS,
				"No-Reply Email", "no-reply@cloud-lbs.com",
				"The &quot;from&quot; email to use when the system "
						+ "sends email to users");

		addSysProp(cat, SystemProperty.KEY_MAIL_FROM_PRETTYNAME,
				"Email From Name", "CloudLBS",
				"The &quot;from&quot; name to use when "
						+ "the system sends email to users");

		cat = SystemProperty.CATEGORY_SECURITY;
		addSysProp(cat, SystemProperty.KEY_ADMIN_PRIVATE_KEY, "Admin Key",
				privateKey, "The base-64 encoded private key to use for "
						+ "signature-authenticated calls as the "
						+ "system's admin user");

		addSysProp(cat, SystemProperty.KEY_ADMIN_PRIVATE_KEY, "Admin Key",
				publicKey, "The base-64 encoded public key to use for "
						+ "signature-authenticated calls as the "
						+ "system's admin user");

		cat = SystemProperty.CATEGORY_XMPP;
		addSysProp(cat, SystemProperty.KEY_XMPP_HOST, "XMPP Host", "localhost",
				"The XMPP host");

		addSysProp(cat, SystemProperty.KEY_XMPP_USERNAME_SUFFIX,
				"XMPP Username Suffix", "localhost",
				"The suffix (e.g. @localhost) to use when referencing an"
						+ " XMPP user");

		addSysProp(cat, SystemProperty.KEY_XMPP_EXTERNAL_HOST,
				"XMPP External Host", "localhost",
				"The XMPP hostname as it would be seen outside the firewall");

		addSysProp(cat, SystemProperty.KEY_XMPP_EXTERNAL_PORT,
				"XMPP External Port", "5222",
				"The XMPP port as mapped outside the firewall");

		addSysProp(cat, SystemProperty.KEY_XMPP_ADMIN_USERNAME,
				"XMPP Username", "cloudlbs", null);

		addSysProp(cat, SystemProperty.KEY_XMPP_ADMIN_PASSWORD,
				"XMPP Password", "cloudlbs", null);

		addSysProp(cat, SystemProperty.KEY_XMPP_MAX_PROCESSORS,
				"Max Processors", "1", "The maximum number of "
						+ "device communication processor instances to "
						+ "support. This is the maximum number "
						+ "of XMPP accounts that may "
						+ "be created for processors");

		cat = SystemProperty.CATEGORY_SERVICES;
		addSysProp(cat, SystemProperty.SERVICE_KEY_PREFIX
				+ SystemPropertyServiceImpl.SERVICE_NAME_KEY
				+ SystemProperty.SERVICE_REMOTE_BASEURL_SUFFIX,
				"System Property Service URL", "http://localhost:8080/svc",
				"The base URL of the system property service");

		addSysProp(cat, SystemProperty.SERVICE_KEY_PREFIX
				+ SystemPropertyServiceImpl.SERVICE_NAME_KEY
				+ SystemProperty.SERVICE_REMOTE_RESOURCESTUB_SUFFIX,
				"System Property Resource URL", "/internal/sysprop",
				"The URL stub for system properties on the remote service");

		addSysProp(cat, SystemProperty.SERVICE_KEY_PREFIX
				+ UserAccountService.SERVICE_NAME_KEY
				+ SystemProperty.SERVICE_REMOTE_BASEURL_SUFFIX,
				"User Account Service URL", "http://localhost:8080/svc",
				"The base URL of the user account service");

		addSysProp(cat, SystemProperty.SERVICE_KEY_PREFIX
				+ UserAccountService.SERVICE_NAME_KEY
				+ SystemProperty.SERVICE_REMOTE_RESOURCESTUB_SUFFIX,
				"User Account Resource URL", "/internal/uacct",
				"The URL stub for user accounts on the remote service");

		addSysProp(cat, SystemProperty.SERVICE_KEY_PREFIX
				+ DeviceService.SERVICE_NAME_KEY
				+ SystemProperty.SERVICE_REMOTE_BASEURL_SUFFIX,
				"Device Service URL", "http://localhost:8080/svc",
				"The base URL of the device service");

		addSysProp(cat, SystemProperty.SERVICE_KEY_PREFIX
				+ DeviceService.SERVICE_NAME_KEY
				+ SystemProperty.SERVICE_REMOTE_RESOURCESTUB_SUFFIX,
				"Device Resource URL", "/internal/device",
				"The URL stub for devices on the remote service");

		addSysProp(cat, SystemProperty.SERVICE_KEY_PREFIX
				+ ApiKeyService.SERVICE_NAME_KEY
				+ SystemProperty.SERVICE_REMOTE_BASEURL_SUFFIX,
				"API Key Service URL", "http://localhost:8080/svc",
				"The base URL of the API key service");

		addSysProp(cat, SystemProperty.SERVICE_KEY_PREFIX
				+ ApiKeyService.SERVICE_NAME_KEY
				+ SystemProperty.SERVICE_REMOTE_RESOURCESTUB_SUFFIX,
				"API Key Resource URL", "/internal/apikey",
				"The URL stub for API keys on the remote service");

		addSysProp(cat, SystemProperty.SERVICE_KEY_PREFIX
				+ AppService.SERVICE_NAME_KEY
				+ SystemProperty.SERVICE_REMOTE_BASEURL_SUFFIX,
				"App Service URL", "http://localhost:8080/svc",
				"The base URL of the app service");

		addSysProp(cat, SystemProperty.SERVICE_KEY_PREFIX
				+ AppService.SERVICE_NAME_KEY
				+ SystemProperty.SERVICE_REMOTE_RESOURCESTUB_SUFFIX,
				"App Resource URL", "/internal/app",
				"The URL stub for apps on the remote service");

		log.debug("Added " + propertiesAdded + " system properties");

	}

	/**
	 * Adds a system property only if there is not already one with the same key
	 * 
	 * @param category
	 * @param key
	 * @param prettyName
	 * @param value
	 * @param description
	 */
	@Transactional
	private void addSysProp(String category, String key, String prettyName,
			String value, String description) {
		long count = systemPropertyDao.countByCriteria(Restrictions.eq("key",
				key));
		if (count == 0) {
			SystemProperty systemProperty = new SystemProperty(UUID
					.randomUUID().toString());
			systemProperty.setKey(key);
			systemProperty.setCategory(category);
			systemProperty.setPrettyName(prettyName);
			systemProperty.setValue(value);
			systemProperty.setDescription(description);
			systemPropertyDao.makePersistent(systemProperty);
			propertiesAdded++;
		}
	}

}

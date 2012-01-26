package com.cloudlbs.platform.domain;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.cloudlbs.core.utils.dao.JpaGenericDao;
import com.cloudlbs.platform.service.internal.SystemPropertyService;

public class SampleData {

	@Autowired
	JpaGenericDao<UserAccount, Long> userAccountDao;

	@Autowired
	JpaGenericDao<Role, Long> roleDao;

	@Autowired
	JpaGenericDao<App, Long> appDao;

	@Autowired
	JpaGenericDao<ApiKey, Long> apiKeyDao;

	@Autowired
	SystemPropertyService systemPropertyService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Transactional
	public void load() {

		UserAccount admin = new UserAccount(UUID.randomUUID().toString(),
				"admin");
		admin.setEmail("admin@cloudlbs.com");
		admin.setFirstName("Root");
		admin.setLastName("Admin");
		admin.setStatus(UserAccount.STATUS_OK);
		// admin.setScopeGuid("ROOT");
		userAccountDao.makePersistent(admin);
		admin.setPassword(passwordEncoder.encodePassword("admin",
				admin.getGuid()));

		Role role = new Role(UUID.randomUUID().toString(), "ROLE_ADMIN");
		roleDao.makePersistent(role);

		admin.addRole(role);

		UserAccount dan = new UserAccount(UUID.randomUUID().toString(),
				"danmascenik");
		dan.setEmail("dan@cloudlbs.co");
		dan.setFirstName("Dan");
		dan.setLastName("Mascenik");
		dan.setStatus(UserAccount.STATUS_OK);
		// dan.setScopeGuid("dan");
		userAccountDao.makePersistent(dan);
		dan.setPassword(passwordEncoder.encodePassword("12345", dan.getGuid()));

		UserAccount brian = new UserAccount(UUID.randomUUID().toString(),
				"brianwitten");
		brian.setEmail("brian@cloudlbs.co");
		brian.setFirstName("Brian");
		brian.setLastName("Witten");
		brian.setStatus(UserAccount.STATUS_OK);
		userAccountDao.makePersistent(brian);
		brian.setPassword(passwordEncoder.encodePassword("12345",
				brian.getGuid()));

		UserAccount joe = new UserAccount(UUID.randomUUID().toString(),
				"joetheplumber");
		joe.setEmail("joe@cloudlbs.co");
		joe.setFirstName("Joe");
		joe.setLastName("Theplumber");
		joe.setStatus(UserAccount.STATUS_PENDING);
		userAccountDao.makePersistent(joe);
		joe.setPassword(passwordEncoder.encodePassword("12345", joe.getGuid()));

		App whereru = new App(UUID.randomUUID().toString());
		whereru.setAndroidPackageName("com.cloudlbs.whereru");
		whereru.setIosBundleId("com.cloudlbs.WhereRU");
		whereru.setName("WhereRU?");
		whereru.setUsesUserAccounts(true);
		appDao.makePersistent(whereru);

		ApiKey whereruKey = new ApiKey(UUID.randomUUID().toString());
		whereruKey.setApp(whereru);
		whereruKey.setCertFingerprint("ABCDEFG");
		whereruKey.setExpirationDate(new Date(
				System.currentTimeMillis() + 1000000l));
		whereruKey.setKeyString("pbJS6bLTQD3oEieKQWl7ltrdvU");
		whereruKey.setPlatform(ApiKey.PLATFORM_ANDROID);
		whereruKey.setSeed("seed");
		apiKeyDao.makePersistent(whereruKey);

		/*
		 * Add the basic, default system properties
		 */
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
	}

	@Transactional
	private void addSysProp(String category, String key, String prettyName,
			String value, String description) {
		SystemProperty systemProperty = new SystemProperty();
		systemProperty.setKey(key);
		systemProperty.setCategory(category);
		systemProperty.setPrettyName(prettyName);
		systemProperty.setValue(value);
		systemProperty.setDescription(description);
		systemPropertyService.createEntity(systemProperty);
	}

}

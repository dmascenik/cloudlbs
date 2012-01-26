package com.cloudlbs.platform.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cloudlbs.core.utils.domain.DatabaseEntity;

/**
 * Represents a key/value used in the configuration of the CloudLBS system.
 * Categories are strings used to group related properties together (see
 * {@link #getCategory()}). Roles refer to the main purpose of an app instance,
 * and are used to determine whether (and for which) service calls should go
 * over the REST API, and which should be handled locally (see #getRole()}).
 * 
 * @author Dan Mascenik
 * 
 */
@Entity
@Table(name = "SYSTEM_PROPERTY")
public class SystemProperty extends DatabaseEntity {

	private static final long serialVersionUID = 1374661665680311750L;

	@Column(name = "KEY", unique = true, nullable = false)
	private String key;

	@Column(name = "PRETTY_NAME", nullable = false)
	private String prettyName;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "VALUE", length = 1024)
	private String value;

	@Column(name = "CATEGORY", nullable = false)
	private String category;

	public SystemProperty() {
	}

	public SystemProperty(String guid) {
		super(guid);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPrettyName() {
		return prettyName;
	}

	public void setPrettyName(String prettyName) {
		this.prettyName = prettyName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * The publicly visible base URL of the system
	 */
	public static final String KEY_BASE_URL_PUBLIC = "system.base.url";

	/**
	 * What to append to the {@link #KEY_BASE_URL_PUBLIC} to get the URL of the
	 * account confirmation servlet
	 */
	public static final String KEY_REGISTER_CONFIRM_URLSTUB = "register.confirm.urlstub";

	/**
	 * The "from" email to use when the system sends email to users
	 */
	public static final String KEY_MAIL_NOREPLY_ADDRESS = "mail.noreply.address";

	/**
	 * The "from" name to use when the system sends email to users
	 */
	public static final String KEY_MAIL_FROM_PRETTYNAME = "mail.from.prettyName";

	/**
	 * The SMTP host to use for sending email
	 */
	public static final String KEY_MAIL_HOST = "mail.host";

	/**
	 * The XMPP host to use for chat communications
	 */
	public static final String KEY_XMPP_HOST = "xmpp.host";

	/**
	 * The XMPP host to use for chat communications
	 */
	public static final String KEY_XMPP_USERNAME_SUFFIX = "xmpp.username.suffix";

	/**
	 * The XMPP hostname as seen outside the firewall
	 */
	public static final String KEY_XMPP_EXTERNAL_HOST = "xmpp.external.host";

	/**
	 * The XMPP port as seen outside the firewall
	 */
	public static final String KEY_XMPP_EXTERNAL_PORT = "xmpp.external.port";

	/**
	 * The XMPP admin user to connect as
	 */
	public static final String KEY_XMPP_ADMIN_USERNAME = "xmpp.admin.username";

	/**
	 * The XMPP admin password
	 */
	public static final String KEY_XMPP_ADMIN_PASSWORD = "xmpp.admin.password";

	/**
	 * The max number of processor instances to support
	 */
	public static final String KEY_XMPP_MAX_PROCESSORS = "xmpp.max.processors";

	/**
	 * The base-64 encoded private key to use for signature-authenticated calls
	 * as the system's admin user
	 */
	public static final String KEY_ADMIN_PRIVATE_KEY = "security.admin.privatekey";

	/**
	 * The base-64 encoded public key to use for signature-authenticated calls
	 * as the system's admin user
	 */
	public static final String KEY_ADMIN_PUBLIC_KEY = "security.admin.publickey";

	/**
	 * Prefix applied to service names to ensure there are no name clashes.
	 */
	public static final String SERVICE_KEY_PREFIX = "service.";

	/**
	 * Suffix applied to the service identifier key to get the system property
	 * key indicating whether it is local or not for a given role.
	 */
	public static final String SERVICE_IS_LOCAL_SUFFIX = ".isLocal";

	/**
	 * Suffix applied to the service identifier key to get the system property
	 * key for the remote service URL.
	 */
	public static final String SERVICE_REMOTE_BASEURL_SUFFIX = ".baseUrl";

	/**
	 * Suffix applied to the service identifier key to get the system property
	 * key for the remote service URL.
	 */
	public static final String SERVICE_REMOTE_RESOURCESTUB_SUFFIX = ".resourceStub";

	/**
	 * Things related to the user-facing webapp
	 */
	public static final String CATEGORY_WEBAPP = "system.webapp";

	/**
	 * Things related to email generated by the system
	 */
	public static final String CATEGORY_MAIL = "system.mail";

	/**
	 * Things related to authentication and authorization
	 */
	public static final String CATEGORY_SECURITY = "system.security";

	/**
	 * Things related to XMPP services
	 */
	public static final String CATEGORY_XMPP = "system.xmpp";

	/**
	 * Things related to backend services
	 */
	public static final String CATEGORY_SERVICES = "system.services";

	/**
	 * This role is for application instances that need to focus their resources
	 * on XMPP connection handling. Many, if not all, service calls would be
	 * sent over the REST API and not handled locally.<br/>
	 * <br/>
	 * Raw text for setting this as an environment property is:
	 * {@value #ROLE_XMPP_HANDLER}
	 */
	public static final String ROLE_XMPP_HANDLER = "xmppHandler";

}

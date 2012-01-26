package com.cloudlbs.platform.core;

import com.cloudlbs.platform.initializer.DatabaseInitializer;

/**
 * 
 * 
 * @author Dan Mascenik
 * 
 */
public class SystemPropertyConstants {

	/**
	 * Set this JVM system property to <code>true</code> to prevent the instance
	 * from attempting any database interactions.
	 */
	public static final String NO_DATABASE_PROPERTY = "noDatabase";

	/**
	 * Set this JVM system property to <code>true</code> to prevent the instance
	 * from attempting any XMPP interactions.
	 */
	public static final String NO_XMPP_PROPERTY = "noXMPP";

	/**
	 * If an instance has no database connectivity, the base URL of the system
	 * properties REST service must be provided; e.g. http://localhost:8080/svc
	 */
	public static final String SYSTEM_PROPERTY_SERVICE_URL = "syspropUrl";

	/**
	 * If an instance has no database connectivity, the resource stub of the
	 * system properties REST service must be provided; e.g. /internal/sysprop
	 */
	public static final String SYSTEM_PROPERTY_RESOURCE_STUB = "syspropStub";

	/**
	 * When setting up the database on startup, setting this JVM system property
	 * will cause API keys to be generated for certain apps (like WhereRU?) by
	 * the {@link DatabaseInitializer} using this certificate fingerprint.<br/>
	 * <br/>
	 * A certificate fingerprint can be obtained like this:
	 * 
	 * <pre>
	 * $ keytool -list -keystore my.keystore -storepass mypassword -keypass mypassword
	 * </pre>
	 * 
	 * Example:
	 * <code>-DandroidCert=BE:BC:47:AD:C5:A5:34:D4:FA:15:D8:3F:01:E3:E6:CE</code>
	 */
	public static final String CLOUDLBS_ANDROID_CERT_FINGERPRINT = "androidCert";
}

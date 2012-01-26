package com.cloudlbs.sls.dao;

/**
 * Various SLS settings are stored as user preferences. Exactly how this is
 * managed on the underlying platform is not important, as long as the settings
 * are only accessible to the SLS and no other apps. If the platform has a
 * native means of storing user preferences in permanent on-board memory (not an
 * SD card), that is ideal.
 * 
 * @author Dan Mascenik
 * 
 */
public interface PreferencesDao {

	public static final String EMULATOR_HOST = "10.0.2.2";
	public static final int EMULATOR_PORT = 8080;

	public static final boolean VALUE_ENABLED_DEF = false;
	public static final boolean VALUE_EMULATOR_MODE_DEF = false;
	public static final String VALUE_MASTER_HOSTNAME_DEF = "dev.cloud-lbs.com";
	public static final int VALUE_MASTER_PORT_DEF = 443;
	public static final boolean VALUE_USE_HTTPS_DEF = true;
	public static final int VALUE_LOG_BUFF_SIZE_DEF = 20;

	/**
	 * Default value is false if nothing has been persisted
	 */
	public boolean getEnabled();

	public void setEnabled(boolean enabled);

	/**
	 * Default value is false if nothing has been persisted. When set to true,
	 * the SLS should use <code>10.0.2.2</code> as the host (the android
	 * emulator's mapping to the host's <code>localhost</code>),
	 * <code>8080</code> for the port, and regular HTTP for web service
	 * connections.
	 */
	public boolean getEmulatorMode();

	public void setEmulatorMode(boolean isEmulatorMode);

	/**
	 * This is the master hostname to use when making HTTP calls and
	 * establishing the XMPP connection. This is how the SLS can be redirected
	 * from a dev environment to QA, production, or other 3rd party environment.
	 * The default value is the CloudLBS production environment.
	 */
	public String getMasterHostname();

	public void setMasterHostname(String masterHostname);

	/**
	 * Whether or not to use HTTPS when making web service calls. The default is
	 * true.
	 */
	public boolean getUseHttps();

	public void setUseHttps(boolean useHttps);

	/**
	 * This is the port to use when making HTTP calls (XMPP will always use the
	 * XMPP default). The default port is 443 for HTTPS in the CloudLBS
	 * production environment.
	 * 
	 */
	public int getMasterPort();

	public void setMasterPort(int port);

	public int getLogBufferSize();

	public void setLogBufferSize(int size);

	/**
	 * Clears all stored settings, returning to defaults.
	 */
	public void clear();

}

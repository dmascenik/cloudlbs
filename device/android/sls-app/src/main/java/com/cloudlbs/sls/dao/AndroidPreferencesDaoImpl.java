package com.cloudlbs.sls.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * This implementation uses Android's {@link SharedPreferences} API to access
 * settings using {@link Context#MODE_PRIVATE}.
 * 
 * @author Dan Mascenik
 * 
 */
public class AndroidPreferencesDaoImpl implements PreferencesDao {

	private static final String KEY_ENABLED = "isEnabled";
	private static final String KEY_EMULATOR_MODE = "emulatorMode";
	private static final String KEY_MASTER_HOSTNAME = "masterHostname";
	private static final String KEY_MASTER_PORT = "masterPort";
	private static final String KEY_USE_HTTPS = "useHttps";
	private static final String KEY_LOG_BUFF_SIZE = "logBufferSize";

	private SharedPreferences settings;

	/**
	 * For testing only
	 */
	protected AndroidPreferencesDaoImpl() {
	}

	public AndroidPreferencesDaoImpl(Context context) {
		settings = context.getSharedPreferences("SLS_PRIVATE",
				Context.MODE_PRIVATE);
	}

	@Override
	public boolean getEnabled() {
		return settings.getBoolean(KEY_ENABLED, VALUE_ENABLED_DEF);
	}

	@Override
	public void setEnabled(boolean enabled) {
		Editor editor = settings.edit();
		editor.putBoolean(KEY_ENABLED, enabled);
		editor.commit();
	}

	@Override
	public boolean getEmulatorMode() {
		return settings.getBoolean(KEY_EMULATOR_MODE, VALUE_EMULATOR_MODE_DEF);
	}

	@Override
	public void setEmulatorMode(boolean isEmulatorMode) {
		Editor editor = settings.edit();
		editor.putBoolean(KEY_EMULATOR_MODE, isEmulatorMode);
		editor.commit();
	}

	@Override
	public String getMasterHostname() {
		return settings.getString(KEY_MASTER_HOSTNAME,
				VALUE_MASTER_HOSTNAME_DEF);
	}

	@Override
	public int getMasterPort() {
		return settings.getInt(KEY_MASTER_PORT, VALUE_MASTER_PORT_DEF);
	}

	@Override
	public void setMasterPort(int port) {
		Editor editor = settings.edit();
		editor.putInt(KEY_MASTER_PORT, port);
		editor.commit();
	}

	@Override
	public void setMasterHostname(String masterHostname) {
		Editor editor = settings.edit();
		editor.putString(KEY_MASTER_HOSTNAME, masterHostname);
		editor.commit();
	}

	@Override
	public boolean getUseHttps() {
		return settings.getBoolean(KEY_USE_HTTPS, VALUE_USE_HTTPS_DEF);
	}

	@Override
	public void setUseHttps(boolean useHttps) {
		Editor editor = settings.edit();
		editor.putBoolean(KEY_USE_HTTPS, useHttps);
		editor.commit();
	}

	@Override
	public int getLogBufferSize() {
		return settings.getInt(KEY_LOG_BUFF_SIZE, VALUE_LOG_BUFF_SIZE_DEF);
	}

	@Override
	public void setLogBufferSize(int size) {
		Editor editor = settings.edit();
		editor.putInt(KEY_LOG_BUFF_SIZE, size);
		editor.commit();
	}

	@Override
	public void clear() {
		Editor editor = settings.edit();
		editor.clear();
		editor.commit();
	}

}

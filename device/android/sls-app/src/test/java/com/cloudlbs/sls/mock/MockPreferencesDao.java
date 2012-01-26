package com.cloudlbs.sls.mock;

import com.cloudlbs.sls.dao.PreferencesDao;

public class MockPreferencesDao implements PreferencesDao {

	private boolean enabled = true;
	private boolean emulatorMode = false;
	private String masterHostname = "localhost";
	private int masterPort = 8080;
	private int logBufferSize = 20;
	private boolean useHttps = false;

	@Override
	public boolean getEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean getEmulatorMode() {
		return emulatorMode;
	}

	@Override
	public void setEmulatorMode(boolean isEmulatorMode) {
		this.emulatorMode = isEmulatorMode;
	}

	@Override
	public String getMasterHostname() {
		return masterHostname;
	}

	@Override
	public void setMasterHostname(String masterHostname) {
		this.masterHostname = masterHostname;
	}

	@Override
	public int getMasterPort() {
		return masterPort;
	}

	@Override
	public void setMasterPort(int port) {
		this.masterPort = port;
	}

	@Override
	public boolean getUseHttps() {
		return useHttps;
	}

	@Override
	public void setUseHttps(boolean useHttps) {
		this.useHttps = useHttps;
	}

	@Override
	public int getLogBufferSize() {
		return logBufferSize;
	}

	@Override
	public void setLogBufferSize(int size) {
		this.logBufferSize = size;
	}

	@Override
	public void clear() {
		this.enabled = true;
		this.emulatorMode = false;
		this.masterHostname = "localhost";
		this.masterPort = 8080;
		this.useHttps = false;
		this.logBufferSize = 20;
	}

}

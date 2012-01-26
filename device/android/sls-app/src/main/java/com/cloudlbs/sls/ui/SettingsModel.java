package com.cloudlbs.sls.ui;

import com.cloudlbs.sls.ui.mvp.Model;

public class SettingsModel extends Model {

	private String hostname;
	private int port;
	private boolean useHttps;
	private int logLines;
	private boolean emulatorMode;

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
		setHasChanges(true);
		notifyViews();
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
		setHasChanges(true);
		notifyViews();
	}

	public boolean isUseHttps() {
		return useHttps;
	}

	public void setUseHttps(boolean useHttps) {
		this.useHttps = useHttps;
		setHasChanges(true);
		notifyViews();
	}

	public int getLogLines() {
		return logLines;
	}

	public void setLogLines(int logLines) {
		this.logLines = logLines;
		setHasChanges(true);
		notifyViews();
	}

	public boolean isEmulatorMode() {
		return emulatorMode;
	}

	public void setEmulatorMode(boolean emulatorMode) {
		this.emulatorMode = emulatorMode;
		setHasChanges(true);
		notifyViews();
	}

}

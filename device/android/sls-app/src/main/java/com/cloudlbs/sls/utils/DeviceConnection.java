package com.cloudlbs.sls.utils;

/**
 * @author Dan Mascenik
 * 
 */
public class DeviceConnection {

	private String deviceUniqueId;
	private String xmppHost;
	private int xmppPort;
	private String xmppUsername;
	private String xmppPassword;
	private String processorName;
	private String otherThing;

	public String getDeviceUniqueId() {
		return deviceUniqueId;
	}

	public void setDeviceUniqueId(String deviceUniqueId) {
		this.deviceUniqueId = deviceUniqueId;
	}

	public String getXmppHost() {
		return xmppHost;
	}

	public void setXmppHost(String xmppHost) {
		this.xmppHost = xmppHost;
	}

	public int getXmppPort() {
		return xmppPort;
	}

	public void setXmppPort(int xmppPort) {
		this.xmppPort = xmppPort;
	}

	public String getXmppUsername() {
		return xmppUsername;
	}

	public void setXmppUsername(String xmppUsername) {
		this.xmppUsername = xmppUsername;
	}

	public String getXmppPassword() {
		return xmppPassword;
	}

	public void setXmppPassword(String xmppPassword) {
		this.xmppPassword = xmppPassword;
	}

	public String getProcessorName() {
		return processorName;
	}

	public void setProcessorName(String processorName) {
		this.processorName = processorName;
	}

	public String getOtherThing() {
		return otherThing;
	}

	public void setOtherThing(String otherThing) {
		this.otherThing = otherThing;
	}

}

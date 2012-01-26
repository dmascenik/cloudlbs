package com.cloudlbs.platform.service;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.springframework.stereotype.Service;

import com.cloudlbs.platform.core.SystemPropertyConstants;
import com.cloudlbs.platform.initializer.XmppInitializer;
import com.cloudlbs.platform.xmpp.XmppConnectionAware;

/**
 * Provides functions that require access to an administrative XMPP account,
 * such as user creation.
 * 
 * @author Dan Mascenik
 * 
 */
@Service
public class XmppAdminService implements XmppConnectionAware {

	/**
	 * This gets injected by the {@link XmppInitializer} an application startup
	 */
	private XMPPConnection connection;

	public static boolean isXmppAvailable() {
		String noXMPPStr = System
				.getProperty(SystemPropertyConstants.NO_XMPP_PROPERTY);
		boolean noXMPP = Boolean.valueOf(noXMPPStr);
		return !noXMPP;
	}

	public static void verifyXmppConnectivity() {
		if (!isXmppAvailable()) {
			throw new RuntimeException(
					"XMPP is not available on this instance!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cloudlbs.platform.services.account.xmpp.XmppAccountManager#createAccount
	 * (java.lang.String, java.lang.String)
	 */
	public void createAccount(String username, String password)
			throws XMPPException {
		verifyXmppConnectivity();
		connection.getAccountManager().createAccount(username, password);
	}

	@Override
	public void setXmppConnection(XMPPConnection connection) {
		this.connection = connection;
	}

}

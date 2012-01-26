package com.cloudlbs.platform.service;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.springframework.stereotype.Service;

import com.cloudlbs.platform.initializer.XmppInitializer;
import com.cloudlbs.platform.xmpp.MultipleLoginWatcher;
import com.cloudlbs.platform.xmpp.XmppConnectionAware;

/**
 * Provides functions that are associated with the messaging XMPP connection,
 * such as roster management.
 * 
 * @author Dan Mascenik
 * 
 */
@Service
public class XmppMessagingService implements XmppConnectionAware {

	/**
	 * This gets injected by the {@link XmppInitializer} an application startup
	 */
	private XMPPConnection connection;

	/**
	 * Returns the username of the XMPP user used by this instance for
	 * messaging.
	 */
	public String getProcessorName() {
		XmppAdminService.verifyXmppConnectivity();
		String processorName = connection.getUser();
		if (processorName.indexOf("@") > 0) {
			processorName = processorName.substring(0,
					processorName.indexOf("@"));
		}
		return processorName;
	}

	/**
	 * Adds a user to this messaging instance's roster.
	 * 
	 * @param username
	 *            in XMPP standard form, user@xmpp.domain
	 * @param nickname
	 */
	public void addRosterEntry(String username, String nickname)
			throws XMPPException {
		XmppAdminService.verifyXmppConnectivity();
		Roster roster = connection.getRoster();
		if (!roster.contains(username)) {
			roster.createEntry(username, nickname, null);
		}
	}

	@Override
	public void setXmppConnection(XMPPConnection connection) {
		this.connection = connection;
		new MultipleLoginWatcher(connection);
	}

}

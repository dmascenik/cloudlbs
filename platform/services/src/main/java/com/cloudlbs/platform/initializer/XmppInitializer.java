package com.cloudlbs.platform.initializer;

import java.util.Collection;
import java.util.Properties;
import java.util.UUID;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cloudlbs.core.utils.Initializable;
import com.cloudlbs.platform.core.SystemPropertyConstants;
import com.cloudlbs.platform.domain.SystemProperty;
import com.cloudlbs.platform.service.XmppAdminService;
import com.cloudlbs.platform.service.internal.SystemPropertyService;
import com.cloudlbs.platform.xmpp.XmppConnectionAware;

/**
 * @author Dan Mascenik
 * 
 */
public class XmppInitializer implements Initializable {

	public static final String PROCESSOR_USER_PREFIX = "processor";

	@Autowired
	private SystemPropertyService systemPropertyService;

	@Autowired
	private XmppAdminService masterXmppService;

	@Autowired
	private XmppConnectionAware xmppAdminService;

	@Autowired
	private XmppConnectionAware xmppMessagingService;

	@Autowired
	private XmppConnectionAware inboundMessageListener;

	@Autowired
	private XmppConnectionAware outboundMessageSender;

	private boolean initialized = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cloudlbs.core.utils.Initializable#initialize()
	 */
	@Override
	public synchronized void initialize() throws Exception {
		if (!initialized) {
			String noXMPPStr = System
					.getProperty(SystemPropertyConstants.NO_XMPP_PROPERTY);
			boolean noXMPP = Boolean.valueOf(noXMPPStr);
			if (noXMPP) {
				/*
				 * XMPP is disabled on this instance
				 */
				log.info(SystemPropertyConstants.NO_XMPP_PROPERTY
						+ " property set.");
				initialized = true;
				return;
			}

			Properties sysProps = systemPropertyService
					.getAsProperties(SystemProperty.CATEGORY_XMPP);
			String xmppHost = sysProps
					.getProperty(SystemProperty.KEY_XMPP_HOST);
			String username = sysProps
					.getProperty(SystemProperty.KEY_XMPP_ADMIN_USERNAME);
			String password = sysProps
					.getProperty(SystemProperty.KEY_XMPP_ADMIN_PASSWORD);

			XMPPConnection adminConnection;
			XMPPConnection messagingConnection;

			/*
			 * --------------------------------------------------------------
			 * 
			 * Create an administrative XMPP connection
			 * 
			 * --------------------------------------------------------------
			 */
			adminConnection = new XMPPConnection(xmppHost);
			log.info("Initializing administrative XMPP connection");
			adminConnection.connect();
			log.info("Connected to XMPP server at " + adminConnection.getHost());
			String hostname = System.getenv("HOSTNAME");
			if (hostname == null) {
				hostname = System.getenv("COMPUTERNAME");
			}
			if (hostname == null) {
				hostname = "UNKNOWN";
			}
			String resourceName = "admin-" + hostname + "-"
					+ UUID.randomUUID().toString();

			adminConnection.login(username, password, resourceName);
			adminConnection.sendPacket(new Presence(Presence.Type.available));
			log.info("Logged in to XMPP server as " + username);

			xmppAdminService.setXmppConnection(adminConnection);

			/*
			 * --------------------------------------------------------------
			 * 
			 * Create an XMPP connection for messaging
			 * 
			 * --------------------------------------------------------------
			 */
			messagingConnection = new XMPPConnection(xmppHost);
			log.info("Initializing messaging XMPP connection");
			messagingConnection.connect();
			Roster roster = adminConnection.getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			String processorHandle = null;
			int rosterMaxIdx = 0;

			/*
			 * Go through the admin user's roster and look for an entry that
			 * starts with "processor" and is currently offline.
			 */
			for (RosterEntry entry : entries) {
				String user = entry.getUser();
				if (user.startsWith(PROCESSOR_USER_PREFIX)) {
					rosterMaxIdx++;
					Presence presence = roster.getPresence(user);
					if (Presence.Type.unavailable.equals(presence.getType())) {
						log.debug("Found inactive processor account " + user);

						// This one's free
						processorHandle = user;
					} else {
						log.debug("Account " + user + " already in use");
					}
				}
			}

			if (processorHandle == null) {
				log.debug("No available processor accounts, "
						+ "creating a new one");

				int maxProcessors = Integer.parseInt(sysProps
						.getProperty(SystemProperty.KEY_XMPP_MAX_PROCESSORS));

				int handleIndex = ++rosterMaxIdx;
				if (handleIndex > maxProcessors) {
					adminConnection.disconnect();
					throw new RuntimeException(
							"Maximum allowed number of processor instances ("
									+ maxProcessors + ") exceeded!");
				}

				/*
				 * No processor XMPP accounts available, need to create a new
				 * one.
				 */
				processorHandle = PROCESSOR_USER_PREFIX + handleIndex;
				masterXmppService.createAccount(processorHandle,
						processorHandle);

				log.debug("Adding " + processorHandle + " to roster for "
						+ adminConnection.getUser());

				String xmppDomain = systemPropertyService.getAsProperties(
						SystemProperty.CATEGORY_XMPP).getProperty(
						SystemProperty.KEY_XMPP_USERNAME_SUFFIX);

				roster.createEntry(processorHandle + "@" + xmppDomain, null,
						null);
			}

			/*
			 * If we found one on the roster, it will be an FQN, but we just
			 * want the username.
			 */
			if (processorHandle.indexOf("@") > 0) {
				processorHandle = processorHandle.substring(0,
						processorHandle.indexOf("@"));
			}

			log.info("Allocated processor XMPP account " + processorHandle);
			String procResourceName = processorHandle + "-" + hostname + "-"
					+ UUID.randomUUID().toString();

			messagingConnection.login(processorHandle, processorHandle,
					procResourceName);
			messagingConnection
					.sendPacket(new Presence(Presence.Type.available));
			log.info("Logged in to XMPP server as " + processorHandle);

			inboundMessageListener.setXmppConnection(messagingConnection);
			outboundMessageSender.setXmppConnection(messagingConnection);
			xmppMessagingService.setXmppConnection(messagingConnection);

			initialized = true;
		}
	}

	private Logger log = LoggerFactory.getLogger(getClass());

}

package com.cloudlbs.platform.xmpp;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudlbs.platform.initializer.XmppInitializer;

/**
 * An instance of this class is created by the {@link XmppInitializer}
 * 
 * @author Dan Mascenik
 * 
 */
public class MultipleLoginWatcher implements PacketListener {

	private String myUsername;

	// Multiple login counter
	private int loginCount;

	public MultipleLoginWatcher(XMPPConnection connection) {
		String user = connection.getUser();
		myUsername = user.substring(0, user.indexOf("@"));
		connection.addPacketListener(this, new PacketFilter() {

			@Override
			public boolean accept(Packet packet) {
				return Presence.class.isAssignableFrom(packet.getClass());
			}
		});
		log.debug("Watching for duplicate logins of " + myUsername);
	}

	@Override
	public void processPacket(Packet packet) {
		Presence presence = (Presence) packet;

		String username = presence.getFrom().substring(0,
				presence.getFrom().indexOf("@"));
		log.debug("Got presence change for " + username);

		if (myUsername.equals(username)) {
			if (Presence.Type.available.equals(presence.getType())) {
				loginCount++;
				log.warn("Multiple (" + loginCount + ") XMPP logins for "
						+ username + "! Possible duplicate handling of data");
			} else if (Presence.Type.unavailable.equals(presence.getType())) {
				loginCount--;
				if (loginCount == 0) {
					log.warn("Duplicate XMPP logins for " + username
							+ " may be resolved "
							+ "(same number of logouts as logins)");
				} else {
					log.warn("An instance of " + username
							+ " has logged out, but " + loginCount
							+ " XMPP login(s) for " + username
							+ "remain(s)! Possible duplicate handling of data");
				}
			}
		} else {
			log.debug("Ignoring presence change for " + username
					+ " since it is not the same as " + myUsername);
		}
	}

	private Logger log = LoggerFactory.getLogger(getClass());

}

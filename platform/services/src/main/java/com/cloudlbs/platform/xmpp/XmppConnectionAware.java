package com.cloudlbs.platform.xmpp;

import org.jivesoftware.smack.XMPPConnection;

public interface XmppConnectionAware {

	public void setXmppConnection(XMPPConnection connection);
	
}

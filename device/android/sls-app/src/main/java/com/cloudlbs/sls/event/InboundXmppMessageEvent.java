package com.cloudlbs.sls.event;

import org.jivesoftware.smack.packet.Message;

/**
 * @author Dan Mascenik
 * 
 */
public class InboundXmppMessageEvent implements SLSEvent {

	private Message message;

	public InboundXmppMessageEvent(Message message) {
		this.message = message;
	}

	public Message getXmppMessage() {
		return message;
	}

}

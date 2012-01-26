package com.cloudlbs.sls.event;


/**
 * @author Dan Mascenik
 * 
 */
public interface InboundXmppMessageListener {

	public void onInboundMessage(InboundXmppMessageEvent event);

}

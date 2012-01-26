package com.cloudlbs.sls.event;


/**
 * @author Dan Mascenik
 * 
 */
public interface OutboundXmppMessageListener {

	public void onOutboundMessage(OutboundXmppMessageEvent event);

}

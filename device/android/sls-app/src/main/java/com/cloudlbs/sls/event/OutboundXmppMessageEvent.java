package com.cloudlbs.sls.event;

import com.cloudlbs.sls.utils.ProtobufMessageConverter;

/**
 * Dispatching an event of this type will put a message on the queue to be
 * transmitted to the XMPP server. Although the payload is just an Object, there
 * must be a corresponding Protobuf message defined for it with a
 * {@link ProtobufMessageConverter} instance registered that can handle it.<br/>
 * <br/>
 * All payloads are converted into Protobuf messages and sent over XMPP as
 * base-64 encoded byte arrays. The Protobuf class name is provided in the
 * message headers so the server can use the right deserializer.
 * 
 * 
 * @author Dan Mascenik
 * 
 */
public class OutboundXmppMessageEvent implements SLSEvent {

	private Object payload;

	public OutboundXmppMessageEvent(Object payload) {
		this.payload = payload;
	}

	public Object getPayload() {
		return payload;
	}

}

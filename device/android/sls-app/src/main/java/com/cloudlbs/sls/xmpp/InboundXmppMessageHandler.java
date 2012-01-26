package com.cloudlbs.sls.xmpp;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.Base64;

import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.InboundLocationEvent;
import com.cloudlbs.sls.event.InboundXmppMessageEvent;
import com.cloudlbs.sls.event.InboundXmppMessageListener;
import com.cloudlbs.sls.location.LocationDataCollection;
import com.cloudlbs.sls.protocol.LocationProto.LocationDataMessage;
import com.cloudlbs.sls.utils.Logger;
import com.cloudlbs.sls.utils.ProtobufMessageConverter;

/**
 * @author Dan Mascenik
 * 
 */
public class InboundXmppMessageHandler implements InboundXmppMessageListener {

	public InboundXmppMessageHandler() {
		EventDispatcher.addListener(this);
	}

	/**
	 * Convert the XMPP message body into a protobuf and dispatch to the
	 * appropriate handler.
	 */
	@Override
	public void onInboundMessage(InboundXmppMessageEvent event) {
		Message msg = event.getXmppMessage();
		String pbClass = (String) msg
				.getProperty(ProcessorClient.MESSAGE_CLASS_HEADER);
		byte[] msgBytes = Base64.decode(msg.getBody());
		com.google.protobuf.Message message = ProtobufMessageConverter
				.parseMessageBytes(msgBytes, pbClass);

		Class<? extends com.google.protobuf.Message> mClz = message.getClass();
		if (mClz.equals(LocationDataMessage.class)) {
			LocationDataMessage loc = (LocationDataMessage) message;
			LocationDataCollection data = ProtobufMessageConverter
					.convertToObject(loc, LocationDataCollection.class);
			EventDispatcher.dispatchEvent(new InboundLocationEvent(data));
		} else {
			Logger.error("Unhandled message class: " + pbClass);
		}
	}
}

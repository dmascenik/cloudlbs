package com.cloudlbs.platform.xmpp;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudlbs.core.utils.protocol.ProtobufMessageConverter;
import com.cloudlbs.platform.domain.SystemProperty;
import com.cloudlbs.platform.initializer.XmppInitializer;
import com.cloudlbs.platform.service.internal.SystemPropertyService;
import com.google.protobuf.Message;

/**
 * The {@link XMPPConnection} used by this class is set by the
 * {@link XmppInitializer}
 * 
 * @author Dan Mascenik
 * 
 */
@Service("outboundMessageSender")
public class OutboundMessageSender implements XmppConnectionAware {

	private XMPPConnection xmppConnection;
	private MessageListener nullListener = new DoNothingMessageListener();
	private Map<String, Chat> chats = new HashMap<String, Chat>();

	@Autowired
	private SystemPropertyService systemPropertyService;

	/**
	 * Currently this assumes that the target device is online with the current
	 * processor. There must be a Protobuf message converter capable of
	 * transforming the provided content object into a protobuf message.
	 * 
	 * @param to
	 * @param content
	 */
	public void send(String to, Object content) {
		Class<? extends Message> mClz = ProtobufMessageConverter
				.getMessageClass(content.getClass());
		if (mClz == null) {
			throw new IllegalArgumentException(
					"No message converter available to handle type "
							+ content.getClass().getName());
		}
		Message contentMessage = ProtobufMessageConverter.fromObject(content);
		send(to, contentMessage);
	}

	/**
	 * Currently this assumes that the target device is online with the current
	 * processor.
	 * 
	 * @param to
	 * @param contentMessage
	 */
	public void send(String to, Message contentMessage) {
		String msgClass = contentMessage.getClass().getName();
		org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
		message.setProperty(InboundMessageListener.MESSAGE_CLASS_HEADER,
				msgClass);
		message.setBody(Base64.encodeBase64String(contentMessage.toByteArray()));
		send(to, message);
	}

	void sendQoSEcho(String to, String body) {
		org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
		message.setProperty(InboundMessageListener.QOS_HEADER, "1");
		message.setBody(body);
		send(to, message);
	}

	public void send(String to, org.jivesoftware.smack.packet.Message message) {
		to = to.toUpperCase();
		try {
			if (isOnline(to)) {
				getChat(to).sendMessage(message);
			} else {
				log.debug("Not sending message - " + to + " is not online");
			}
		} catch (XMPPException e) {
			log.error("Sending message to device " + to + " failed", e);
		}
	}

	private boolean isOnline(String user) {
		user = user.toUpperCase();

		String xmppDomain = systemPropertyService.getAsProperties(
				SystemProperty.CATEGORY_XMPP).getProperty(
				SystemProperty.KEY_XMPP_USERNAME_SUFFIX);

		Presence p = xmppConnection.getRoster().getPresence(
				user + "@" + xmppDomain);
		return p.isAvailable();
	}

	@Override
	public void setXmppConnection(XMPPConnection xmppConnection) {
		this.xmppConnection = xmppConnection;
	}

	public synchronized Chat getChat(String to) {
		to = to.toUpperCase();
		Chat chat = chats.get(to);
		if (chat == null) {
			String xmppDomain = systemPropertyService.getAsProperties(
					SystemProperty.CATEGORY_XMPP).getProperty(
					SystemProperty.KEY_XMPP_USERNAME_SUFFIX);

			chat = xmppConnection.getChatManager().createChat(
					to + "@" + xmppDomain, nullListener);
			chats.put(to, chat);
		}
		return chat;
	}

	class DoNothingMessageListener implements MessageListener {

		@Override
		public void processMessage(Chat arg0,
				org.jivesoftware.smack.packet.Message arg1) {
			// does nothing
		}
	}

	private Logger log = LoggerFactory.getLogger(getClass());
}

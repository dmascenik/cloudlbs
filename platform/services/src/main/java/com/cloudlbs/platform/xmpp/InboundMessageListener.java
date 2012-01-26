package com.cloudlbs.platform.xmpp;

import org.bouncycastle.util.encoders.Base64;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudlbs.core.utils.events.EventDispatcher;
import com.cloudlbs.core.utils.protocol.ProtobufMessageConverter;
import com.cloudlbs.platform.domain.LocationReading;
import com.cloudlbs.platform.initializer.XmppInitializer;

/**
 * The {@link XMPPConnection} used by this class is set by the
 * {@link XmppInitializer}
 * 
 * @author Dan Mascenik
 * 
 */
@Service
public class InboundMessageListener implements XmppConnectionAware,
		PacketListener {

	public static final String MESSAGE_CLASS_HEADER = "Protobuf-Type";
	public static final String QOS_HEADER = "QoS";

	@Autowired
	private EventDispatcher eventDispatcher;

	@Autowired
	private OutboundMessageSender deviceMessageSender;

	private XMPPConnection xmppConnection;

	private boolean isInitialized = false;

	public synchronized void initialize() {
		if (!isInitialized) {
			xmppConnection.addPacketListener(this, new PacketFilter() {

				@Override
				public boolean accept(Packet packet) {

					/*
					 * Only listens for messages (not presence)
					 */
					return Message.class.isAssignableFrom(packet.getClass());
				}
			});

			isInitialized = true;
		}
	}

	@Override
	public void processPacket(Packet packet) {
		try {
			Message msg = (Message) packet;
			String pbClass = (String) msg.getProperty(MESSAGE_CLASS_HEADER);
			String toProcessor = msg.getTo();
			if (toProcessor.indexOf("@") > 0) {
				toProcessor = toProcessor
						.substring(0, toProcessor.indexOf("@"));
			}
			String fromDevice = msg.getFrom();
			if (fromDevice.indexOf("@") > 0) {
				fromDevice = fromDevice.substring(0, fromDevice.indexOf("@"));
			}
			// String msgId = msg.getPacketID();

			/*
			 * Handle QoS messages quickly and return
			 */
			String qosHeader = (String) msg.getProperty(QOS_HEADER);
			if (qosHeader != null && qosHeader.trim().length() > 0) {
				log.debug("Echoing QoS message back to " + fromDevice);

				// Echo the message back immediately
				deviceMessageSender.sendQoSEcho(fromDevice, msg.getBody());

				// that's all for a QoS message
				return;
			}

			byte[] msgBytes = Base64.decode(msg.getBody());
			com.google.protobuf.Message message = ProtobufMessageConverter
					.parseMessageBytes(msgBytes, pbClass);

			if (log.isDebugEnabled()) {
				StringBuffer sb = new StringBuffer("\nFROM: " + msg.getFrom());
				sb.append("\nTO: " + msg.getTo());
				sb.append("\nTYPE: " + pbClass);
				sb.append("\n" + message.toString() + "\n");
				log.debug(sb.toString());
			}

			Object obj = ProtobufMessageConverter.toObject(message);
			log.debug("Converted to " + obj.getClass().getName());
			if (LocationReading.class.isAssignableFrom(obj.getClass())) {
				LocationReading reading = (LocationReading) obj;

				/*
				 * Hack for the HackeyLocationSharingService
				 */
				if (!"TESTROBOT".equals(fromDevice.toUpperCase())) {
					reading.setSubjGuid(fromDevice);
				}

				eventDispatcher.dispatchEvent(reading);
			} else {
				throw new IllegalArgumentException(
						"Unknown incoming message type: "
								+ obj.getClass().getName());
			}
		} catch (Exception e) {
			/*
			 * The Smack API seems to catch any exceptions and swallow them
			 * silently. At least this way they get logged.
			 */
			String s = "Exception handling incoming XMPP message";
			log.error(s, e);
			throw new RuntimeException(s, e);
		}
	}

	@Override
	public void setXmppConnection(XMPPConnection xmppConnection) {
		this.xmppConnection = xmppConnection;
		initialize();
	}

	private Logger log = LoggerFactory.getLogger(getClass());
}

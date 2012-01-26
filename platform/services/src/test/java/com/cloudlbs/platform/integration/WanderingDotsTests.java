package com.cloudlbs.platform.integration;

import junit.framework.TestCase;

import org.apache.commons.codec.binary.Base64;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.junit.Before;
import org.junit.Test;

import com.cloudlbs.core.utils.protocol.MessageConverter;
import com.cloudlbs.core.utils.protocol.ProtobufMessageConverter;
import com.cloudlbs.platform.domain.LocationReading;
import com.cloudlbs.platform.xmpp.InboundMessageListener;
import com.cloudlbs.sls.protocol.LocationProto.LocationReadingMessage;

/**
 * @author Dan Mascenik
 * 
 */
public class WanderingDotsTests extends TestCase {

	private MessageConverter<LocationReadingMessage, LocationReading> conv = new ProtobufMessageConverter<LocationReadingMessage, LocationReading>(
			LocationReadingMessage.class, LocationReading.class);

	private Chat processorChat;

	private static final double INCR = 0.00015;

	@Test
	public synchronized void testSendSomeLocations() throws Exception {
		XMPPConnection conn = new XMPPConnection("dev.cloud-lbs.com");
		// XMPPConnection conn = new XMPPConnection("localhost");
		conn.connect();

		conn.login("testrobot", "password");

		processorChat = conn.getChatManager().createChat(
				"processor1@dev.cloud-lbs.com", new MessageListener() {

					@Override
					public void processMessage(Chat c, Message msg) {
						System.out.println("Received message: " + msg.getBody());
					}
				});

		double latInit = 38.9594;
		double lonInit = -77.3604;
		double lat0 = latInit - 0.001;
		double lon0 = lonInit;
		double lat1 = latInit;
		double lon1 = lonInit;

		int dir0 = 3; // 0,1,2,3 are E,S,W,N
		int dir1 = 0; // 0,1,2,3 are E,S,W,N

		while (true) {

			double[] loc0 = getNextPoint(latInit, lonInit, lat0, lon0, dir0);
			double[] loc1 = getNextPoint(latInit, lonInit, lat1, lon1, dir1);
			lat0 = loc0[0];
			lon0 = loc0[1];
			dir0 = (int) loc0[2];
			lat1 = loc1[0];
			lon1 = loc1[1];
			dir1 = (int) loc1[2];

			sendReading("0000001-CDMA", lat0, lon0);
			wait(500);

			sendReading("0000002-CDMA", lat1, lon1);
			wait(500);
		}
	}

	private void sendReading(String from, double lat, double lon)
			throws Exception {
		LocationReading lr = new LocationReading();
		lr.setAltitude(0.0);
		lr.setErrorRadius(12.0f);
		lr.setFixTime(0);
		lr.setSubjGuid(from.toUpperCase());
		lr.setLatitude(lat);
		lr.setLongitude(lon);
		lr.setTimedOut(false);
		lr.setTimestamp(System.currentTimeMillis());
		lr.setApiKey("k7n8SGcC0hVLML6CqdYy18wvFs"); // dev
		// lr.setApiKey("PreSDlnGVul0jTfohXgKK4gg"); // local

		LocationReadingMessage msg = conv.toMessage(lr);
		String msgString = new String(Base64.encodeBase64(msg.toByteArray()));
		String msgClass = msg.getClass().getName();
		Message xmppMessage = new Message();
		xmppMessage.setBody(msgString);
		xmppMessage.setProperty(InboundMessageListener.MESSAGE_CLASS_HEADER,
				msgClass);
		processorChat.sendMessage(xmppMessage);
	}

	/**
	 * Moves the point in a clockwise square from the starting point.
	 * 
	 * @param latInit
	 * @param lonInit
	 * @param lat
	 * @param lon
	 * @param dir
	 * @return
	 */
	private double[] getNextPoint(double latInit, double lonInit, double lat,
			double lon, int dir) {

		switch (dir) {
		case 0:
			lon += INCR;
			if (lon >= -77.35685) {
				lon = -77.35685;
				dir = 1;
			}
			break;

		case 1:
			lat -= INCR;
			if (lat <= 38.95785) {
				lat = 38.95785;
				dir = 2;
			}
			break;

		case 2:
			lon -= INCR;
			if (lon <= lonInit) {
				lon = lonInit;
				dir = 3;
			}
			break;

		case 3:
			lat += INCR;
			if (lat >= latInit) {
				lat = latInit;
				dir = 0;
			}
			break;

		default:
			break;
		}
		return new double[] { lat, lon, dir };
	}

	boolean initialized = false;

	@Before
	public void before() throws Exception {
		if (!initialized) {
		}
	}
}

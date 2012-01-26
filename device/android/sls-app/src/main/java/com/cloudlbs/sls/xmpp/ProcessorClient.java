package com.cloudlbs.sls.xmpp;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.codec.binary.Base64;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;

import android.content.BroadcastReceiver;

import com.cloudlbs.sls.dao.AndroidPreferencesDaoImpl;
import com.cloudlbs.sls.dao.OutboundMessageDao;
import com.cloudlbs.sls.dao.PhoneInfoDao;
import com.cloudlbs.sls.dao.PreferencesDao;
import com.cloudlbs.sls.event.ActivationListener;
import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.InboundXmppMessageEvent;
import com.cloudlbs.sls.event.NetworkStatusEvent;
import com.cloudlbs.sls.event.OutboundXmppMessageEvent;
import com.cloudlbs.sls.event.OutboundXmppMessageListener;
import com.cloudlbs.sls.event.ShutdownEvent;
import com.cloudlbs.sls.event.StartupEvent;
import com.cloudlbs.sls.http.DeviceRegistrationRemoteService;
import com.cloudlbs.sls.protocol.DeviceConnectionProto.DeviceConnectionMessage;
import com.cloudlbs.sls.utils.Assert;
import com.cloudlbs.sls.utils.Logger;
import com.cloudlbs.sls.utils.ProtobufMessageConverter;

/**
 * The XMPP connection can be in one of 3 states: disconnected, trying to
 * connect, and connected. This class manages the state of the XMPP connection
 * and strives to keep it active whenever possible. <br/>
 * </br> This class is abstract, so subclasses are expected to provide the best
 * native logic for implementing abstract functions.<br/>
 * <br/>
 * Here's the basic logic:</br><br/>
 * <ol>
 * <li>On instantiation, the instance either goes into a sleep state because it
 * has nothing more to do (is disabled, no network), or tries to establish an
 * XMPP connection.<br/>
 * <br/>
 * </li>
 * <li>When trying to connect, first call the
 * {@link DeviceRegistrationRemoteService} to get the connection details.<br/>
 * <br/>
 * </li>
 * <li>Connect to the XMPP server indicated by the {@link PreferencesDao}. If in
 * emulator mode, use <code>10.0.2.2</code>. Login with the credentials obtained
 * in the connection details.<br/>
 * <br/>
 * </li>
 * <li>Add the designated processor to this device's roster (a.k.a. buddy list).
 * <br/>
 * <br/>
 * </li>
 * <li>Verify the presence of the designated processor. It should be online
 * since the server just designated it in the connection details, but it may
 * have JUST gone offline.<br/>
 * <br/>
 * <li>When the presence of the processor is established by the roster listener,
 * a chat is established between this device and the processor. The chat gets a
 * message listener and the outbound message queue is created. Now everything
 * should be A-OK. XMPP messages can go out and in.<br/>
 * <br/>
 * </li>
 * <li>If anything strange happens (e.g. a network change/drop), a quality of
 * service ping is sent to the XMPP server. One is sent every couple minutes
 * anyway to be sure the connection is still good. If the server does not send
 * back an acknowledgment within a reasonable amount, the XMPP connection gets
 * recycled and the whole things starts over.</li>
 * </ol>
 * 
 * @author Dan Mascenik
 * 
 */
public abstract class ProcessorClient implements ActivationListener,
		OutboundXmppMessageListener, NetworkStatusListener {

	public static final int STATUS_ON_NETWORK = 1;
	public static final int STATUS_HAS_PROCESSOR = 2;
	public static final int STATUS_XMPP_CONNECTED = 3;
	public static final int STATUS_XMPP_AUTHED = 4;
	public static final int STATUS_HAS_ROSTER = 5;
	public static final int STATUS_PROCESSOR_ONLINE = 6;
	public static final int STATUS_HAS_CHAT = 7;

	public static final String MESSAGE_CLASS_HEADER = "Protobuf-Type";
	public static final String QOS_HEADER = "QoS";

	/**
	 * Constructor-provided - used to determine if the SLS is enabled and what
	 * host to connect to.
	 */
	private PreferencesDao preferencesDao;
	private OutboundMessageDao outboundMessageDao;
	private PhoneInfoDao phoneInfoDao;
	private DeviceRegistrationRemoteService deviceConnectionRemoteService;

	/*
	 * State fields managed by this class
	 */
	private XMPPConnection conn;
	private String processorName;
	private Chat processorChat;
	private ProcessorRosterListener rosterListener;
	private ConcurrentLinkedQueue<com.google.protobuf.Message> outboundQueue;
	private State state = State.DISCONNECTED;

	/**
	 * During a quality of service check, the outbound message queue may be
	 * locked to ensure that no messages are sent to a connection that appears
	 * valid but isn't responding.
	 */
	private boolean qosLocked = false;
	private boolean qosEchoRcvd = false;

	/**
	 * If multiple QoS triggers go off, the first will set the QoS lock, but the
	 * last QoS thread launched may release it. Only one QoS thread needs to be
	 * running at a time, so a new one is free to interrupt any prior one and
	 * take its place.
	 */
	private Thread qosThread;

	/**
	 * Override this in a superclass if you want to ignore resource names (as
	 * you would when running local integration tests).
	 */
	protected boolean ignoreResource = false;

	/**
	 * Instantiate and immediately attempt to create a connection if the stored
	 * preference is for the service to be enabled. After that, listen for
	 * {@link StartupEvent}s and {@link ShutdownEvent}s to toggle the
	 * connection.
	 * 
	 * @param preferencesDao
	 * @param outboundMessageDao
	 * @param phoneInfoDao
	 * @param deviceConnectionRemoteService
	 */
	public ProcessorClient(PreferencesDao preferencesDao,
			OutboundMessageDao outboundMessageDao, PhoneInfoDao phoneInfoDao,
			DeviceRegistrationRemoteService deviceConnectionRemoteService) {
		Assert.notNull(preferencesDao);
		Assert.notNull(outboundMessageDao);
		Assert.notNull(phoneInfoDao);
		Assert.notNull(deviceConnectionRemoteService);
		this.preferencesDao = preferencesDao;
		this.outboundMessageDao = outboundMessageDao;
		this.phoneInfoDao = phoneInfoDao;
		this.deviceConnectionRemoteService = deviceConnectionRemoteService;

		connect();

		EventDispatcher.addListener(this);
	}

	/**
	 * Do the actual XMPP connection based on the current settings from the
	 * {@link PreferencesDao}. The state must be DISCONNECTED when this method
	 * is called, and all the stateful fields set to null. The
	 * {@link #getState()} will return TRYING while this method is running, and
	 * either CONNECTED or DISCONNECTED after it completes. If the SLS is
	 * disabled, this method does nothing.<br/>
	 * <br/>
	 * NOTE: calling this method will overwrite the outbound message queue with
	 * the stored queue data.
	 */
	protected synchronized void connect() {
		if (state == State.CONNECTED || conn != null || processorChat != null
				|| processorName != null || outboundQueue != null
				|| rosterListener != null) {
			/*
			 * Something didn't get cleaned up right. There could be unexpected
			 * behaviors if we continue.
			 */
			throw new IllegalStateException("Client already connected - "
					+ "disconnect first");
		} else {

			/*
			 * Don't do anything if the SLS is disabled.
			 */
			if (!preferencesDao.getEnabled()) {
				Logger.info("Not creating XMPP connection (SLS disabled)");
				return;
			}

			/*
			 * Don't bother doing anything if there's no network.
			 */
			if (!isNetworkAvailable()) {
				Logger.info("Not creating XMPP connection (no network)");
				return;
			}
		}

		state = State.TRYING;
		try {
			Logger.debug("Creating new XMPP connection");

			/*
			 * Connection details requires making an HTTP call.
			 */
			DeviceConnectionMessage dcm = getConnectionDetails();
			String host;
			if (preferencesDao.getEmulatorMode()) {
				host = AndroidPreferencesDaoImpl.EMULATOR_HOST;
			} else {
				host = preferencesDao.getMasterHostname();
			}
			conn = connectAndLogin(host, dcm.getXmppUsername(),
					dcm.getXmppPassword(), dcm.getDeviceUniqueId());

			/*
			 * Set up the roster to handle just the designated processor.
			 */
			processorName = dcm.getProcessorName();
			setupRoster(conn, processorName);

			/*
			 * Create the chat session
			 */
			processorChat = conn.getChatManager().createChat(processorName,
					new ProcessorMessageListener());

			/*
			 * Add the roster listener and verify the presence of the designated
			 * processor.
			 */
			rosterListener = new ProcessorRosterListener();
			conn.getRoster().addRosterListener(rosterListener);

			/*
			 * Restore the outbound message queue
			 */
			restoreOutboundMessages();

			state = State.CONNECTED;

			if (isProcessorOnline()) {
				rosterListener.isProcessorOnline = true;
				Logger.info("XMPP connection success to " + processorName);
				flush();
			} else {
				/*
				 * This is a weird state. Something may be going on with the
				 * network connectivity, or the processor may have just gone
				 * offline. Will have to wait for something else to trigger a
				 * reconnection maybe.
				 */
				rosterListener.isProcessorOnline = false;
				Logger.info("XMPP connected to " + host + " but "
						+ processorName + " is offline");
			}
		} catch (Exception e) {
			Logger.debug("Exception connecting to XMPP:", e);
			Logger.warn("XMPP connection failed");

			/*
			 * Clean up
			 */
			disconnect();
			state = State.DISCONNECTED;
		}
	}

	/**
	 * Disconnects from XMPP and releases all resources.
	 */
	protected synchronized void disconnect() {
		state = State.DISCONNECTED;
		saveOutboundMessageQueue();
		if (conn != null) {
			Logger.debug("Shutting down XMPP connection");
			conn.getRoster().removeRosterListener(rosterListener);
			if (conn.isAuthenticated()) {
				conn.sendPacket(new Presence(Presence.Type.unavailable));
				conn.disconnect();
			} else {
				conn.disconnect();
			}
			conn = null;
			Logger.debug("XMPP connection cleared");
		}
		conn = null;
		processorName = null;
		processorChat = null;
		outboundQueue = null;
		rosterListener = null;
	}

	/**
	 * Do the actual XMPP connection and login.
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @param resourceName
	 */
	protected static XMPPConnection connectAndLogin(String host,
			String username, String password, String resourceName)
			throws XMPPException {

		/*
		 * This default configuration may need work in the future. For now it
		 * just sets all security, debug and compression options to "off" and
		 * forces a presence packet to be sent on connection.
		 */
		ConnectionConfiguration config = new ConnectionConfiguration(host, 5222);
		config.setCompressionEnabled(false);
		config.setDebuggerEnabled(false);
		config.setExpiredCertificatesCheckEnabled(false);
		config.setNotMatchingDomainCheckEnabled(false);
		config.setReconnectionAllowed(false);
		config.setRosterLoadedAtLogin(true);
		config.setSASLAuthenticationEnabled(false);
		config.setSelfSignedCertificateEnabled(false);
		config.setSendPresence(true);
		config.setVerifyChainEnabled(false);
		config.setVerifyRootCAEnabled(false);
		XMPPConnection c = new XMPPConnection(config);

		/*
		 * Try actually connecting to the XMPP server.
		 */
		try {
			c.connect();
		} catch (XMPPException e) {
			Logger.error("Could not connect to XMPP server at " + c.getHost()
					+ ":" + c.getPort());
			throw e;
		}

		/*
		 * Try logging in with the credentials that came from the connection
		 * details HTTP call.
		 */
		try {

			c.login(username, password, resourceName);
			c.sendPacket(new Presence(Type.available, null, 128, Mode.available));
		} catch (XMPPException e) {
			Logger.error("Could not authenticate with XMPP server (" + username
					+ "/" + resourceName + ")");
			throw e;
		}

		return c;
	}

	/**
	 * Set up the roster (a.k.a. buddy list). Remove any processor but the
	 * designated one from this connection's roster to minimize network traffic
	 * for uninteresting presence packets. This device only cares about the
	 * presence of its designated processor.
	 * 
	 * @param connection
	 * @param processorName
	 *            e.g. username@myxmpphost.com
	 */
	protected static void setupRoster(XMPPConnection connection,
			String processorName) throws XMPPException {
		String rosterName = getProcessorRosterName(processorName);
		String processorNickName = getProcessorNickname(processorName);
		Roster roster = connection.getRoster();
		Collection<RosterEntry> res = roster.getEntries();
		RosterEntry[] entries = res.toArray(new RosterEntry[0]);
		for (int i = 0; i < entries.length; i++) {
			RosterEntry entry = entries[i];
			if (!entry.getUser().equals(processorName)) {
				try {
					roster.removeEntry(entry);
				} catch (XMPPException e) {
					Logger.error("Could not remove " + entry.getUser()
							+ " from roster");
					throw e;
				}
			}
		}
		if (!roster.contains(processorName)) {
			try {
				roster.createEntry(rosterName, processorNickName, null);
			} catch (XMPPException e) {
				Logger.error("Could not add " + rosterName + " to roster");
				throw e;
			}
		}
	}

	/**
	 * Restore the outbound message queue from the persisted state (if any).
	 */
	private void restoreOutboundMessages() {
		try {
			outboundQueue = outboundMessageDao.loadOutboundMessageQueue();
			if (outboundQueue.size() > 0) {
				Logger.info("Restored " + outboundQueue.size()
						+ " messages to the queue");
			}
			/*
			 * Clear the stored queue so it doesn't get reloaded later.
			 */
			outboundMessageDao
					.saveOutboundMessageQueue(new ConcurrentLinkedQueue<com.google.protobuf.Message>());
		} catch (IOException e) {
			Logger.error("Failed to load stored messages: " + e.getMessage());
			outboundQueue = new ConcurrentLinkedQueue<com.google.protobuf.Message>();
		}
	}

	/**
	 * Writes the outbound message queue to persistent memory.
	 */
	private void saveOutboundMessageQueue() {
		try {
			if (outboundQueue != null) {
				outboundMessageDao.saveOutboundMessageQueue(outboundQueue);
				Logger.info("Successfully backed up " + outboundQueue.size()
						+ " outbound messages");
			}
		} catch (IOException e) {
			Logger.error("Outbound message queue backup failed");
		}
	}

	/**
	 * Put a message on the outbound queue. This causes the entire queue to try
	 * to {@link #flush()} immediately.
	 * 
	 * @param message
	 */
	@Override
	public void onOutboundMessage(OutboundXmppMessageEvent event) {
		if (state != State.CONNECTED) {
			Logger.warn("SLS disabled - outbound message dropped");
			return;
		}
		com.google.protobuf.Message message = ProtobufMessageConverter
				.convertToMessage(event.getPayload());
		outboundQueue.add(message);
		flush();
	}

	/**
	 * Attempts to send all the messages in the queue (in queue order).If the
	 * client can't send the message, it should return false, and the message
	 * will remain on the queue.
	 */
	private synchronized void flush() {
		if (outboundQueue == null || outboundQueue.isEmpty()) {
			// nothing to do
			return;
		}
		int i = 0;
		while (!outboundQueue.isEmpty()) {
			com.google.protobuf.Message m = outboundQueue.peek();
			String msgString = formatMessage(m);
			String msgClass = m.getClass().getName();
			Message xmppMessage = new Message();
			xmppMessage.setBody(msgString);
			xmppMessage.setProperty(MESSAGE_CLASS_HEADER, msgClass);
			boolean success = sendMessage(xmppMessage);
			if (!success) {
				break;
			} else {
				i++;
				outboundQueue.poll();
			}
		}
		Logger.info("Transmitted " + i + " messages");
	}

	/**
	 * Send a message to the processor, checking if the processor is online
	 * first. Returns true if the message was sent successfully. This may throw
	 * an exception if the connection is not up or is not authenticated.
	 * 
	 * @param message
	 */
	boolean sendMessage(Message message) {
		boolean result = false;
		try {
			if (state == State.CONNECTED && isProcessorOnline() && !qosLocked) {
				processorChat.sendMessage(message);
				result = true;
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	/**
	 * An XMPP message to the server is formatted as a base-64 encoded string of
	 * the protobuf's byte[].
	 * 
	 * @param m
	 */
	String formatMessage(com.google.protobuf.Message m) {
		return new String(Base64.encodeBase64(m.toByteArray()));
	}

	/**
	 * Returns the XMPP connection details, possibly from an HTTP call.
	 * 
	 * @see DeviceRegistrationRemoteService
	 */
	protected DeviceConnectionMessage getConnectionDetails() throws Exception {
		return deviceConnectionRemoteService.get(phoneInfoDao
				.getDeviceUniqueId());
	}

	/**
	 * Uses the native API to determine if the device has network connectivity
	 * or not.
	 */
	protected abstract boolean isNetworkAvailable();

	/**
	 * It is highly recommended that network status changes are detected and
	 * {@link #qosCheck()} called when one occurs. As the device switches from
	 * 3G to Wifi, for example, it's possible for an XMPP connection to appear
	 * valid when it's not.
	 */
	@Override
	public void onNetworkStatusChange(NetworkStatusEvent evt) {
		if (isNetworkAvailable()) {
			if (getState() == State.CONNECTED) {
				Logger.debug("QoS check on network status change");
				qosCheck();
			} else if (getState() == State.DISCONNECTED) {
				Logger.debug("Attempting reconnect to network");
				connect();
			}
		} else {
			Logger.debug("Disconnecting to save resources (no network)");
			disconnect();
		}
	}

	/**
	 * If a network status change happens, the connection may be broken. Verify
	 * the connection with a QoS message. This temporarily stops outbound
	 * messages until the server responds to the QoS message. If no response is
	 * received after some time, reconnection is attempted.
	 */
	protected void qosCheck() {
		if (qosThread != null && qosThread.isAlive()) {
			qosThread.interrupt();
		}

		qosLocked = true;
		qosEchoRcvd = false;
		Runnable qosRunnable = new Runnable() {

			@Override
			public synchronized void run() {
				for (int pass = 0; pass < 5; pass++) {
					try {
						wait(1000);
					} catch (InterruptedException e) {
						// Another QoS thread is taking over.
						return;
					}
					// Did the message come back?
					if (qosEchoRcvd) {
						Logger.info("Connection confirmed with "
								+ processorName);

						// unlock and flush the outbound queue
						qosLocked = false;
						flush();
						return;
					}
				}

				// No response after 5s, reconnect.
				disconnect();
				connect();
			}

		};

		/*
		 * Send the QoS message
		 */
		if (state == State.CONNECTED && isProcessorOnline()) {
			Message message = new Message();
			message.setBody("QoS");
			message.setProperty(QOS_HEADER, "1");
			try {
				Logger.debug("Sending QoS test message");
				processorChat.sendMessage(message);
			} catch (XMPPException e) {
			}
		}

		/*
		 * Wait for the echo
		 */
		qosThread = new Thread(qosRunnable);
		qosThread.start();
	}

	public State getState() {
		return state;
	}

	@Override
	public synchronized void onShutdown(ShutdownEvent evt) {
		if (state != State.DISCONNECTED) {
			disconnect();
		}
	}

	@Override
	public synchronized void onStartupEvent(StartupEvent evt) {
		if (state != State.CONNECTED) {
			connect();
		}
	}

	static String getProcessorNickname(String processorName) {
		return processorName.substring(0, processorName.indexOf("@"));
	}

	static String getProcessorRosterName(String processorName) {
		int idx = processorName.indexOf("/");
		String rosterName;
		if (idx > 0) {
			rosterName = processorName.substring(0, idx);
		} else {
			rosterName = processorName;
		}
		return rosterName;
	}

	/**
	 * Checks that the designated processor is on the {@link Roster} for this
	 * {@link XMPPConnection} and is online. This will always say "unavailable"
	 * immediately upon startup due to the asynchronous nature of presence.
	 */
	protected boolean isProcessorOnline() {
		Roster roster = conn.getRoster();
		RosterEntry processor = roster.getEntry(processorName);
		if (processor == null) {
			return false;
		}
		Presence presence = roster.getPresence(processorName);
		if (presence == null) {
			return false;
		}
		return Presence.Type.available.equals(presence.getType());
	}

	/**
	 * Handler for messages coming in from the designated processor.
	 * 
	 * @author Dan Mascenik
	 * 
	 */
	class ProcessorMessageListener implements MessageListener {

		/**
		 * Incoming XMPP messages are dispatched to the {@link EventDispatcher}
		 * to be forwarded to anything that listens for
		 * {@link InboundXmppMessageEvent} s.
		 */
		@Override
		public void processMessage(Chat chat, Message message) {
			String rawUser = message.getFrom();
			String username = getProcessorRosterName(rawUser);

			if (!username.equals(processorName)) {
				/*
				 * Not from the designated processor.
				 */
				Logger.warn("Dropping incoming XMPP message from " + username);
				return;
			}

			/*
			 * Capture if this was a QoS echo message
			 */
			if (qosLocked && message.getBody().equals("QoS")) {
				Logger.debug("Received QoS message echo");
				qosEchoRcvd = true;
				return;
			}

			Logger.debug("XMPP IN: " + message.getBody());
			EventDispatcher.dispatchEvent(new InboundXmppMessageEvent(message));
		}

	}

	/**
	 * This roster listener only handles presence changes for processors. If the
	 * designated processor goes offline, the XMPP client must reconnect to
	 * establish a chat with a new processor.
	 * 
	 * @author Dan Mascenik
	 * 
	 */
	class ProcessorRosterListener implements RosterListener {

		boolean isProcessorOnline = true;

		@Override
		public synchronized void presenceChanged(Presence presence) {
			String rawUser = presence.getFrom();
			String username = getProcessorRosterName(rawUser);
			String processorNickName = getProcessorNickname(processorName);

			/*
			 * Server resource names start with the processorN-. Any other
			 * presence changes (e.g. from a human chat client for debugging)
			 * should be ignored.
			 */
			String resourceName = rawUser.substring(rawUser.indexOf("/") + 1);

			if (username.equals(processorName)) {
				if (!ignoreResource
						&& !resourceName.startsWith(processorNickName + "-")) {

					/*
					 * This presence change is for a non-processor, so the XMPP
					 * client doesn't need to do anything.
					 */
					Logger.debug("Ignoring " + username
							+ " presence for resource \"" + resourceName + "\"");
					return;
				}

				if (Presence.Type.unavailable.equals(presence.getType())) {
					if (!isProcessorOnline) {
						// already know
						return;
					} else {
						Logger.warn(processorNickName + " has gone offline");

						// Try to reconnect
						disconnect();
						connect();
					}
				} else if (Presence.Type.available.equals(presence.getType())) {
					if (isProcessorOnline) {
						// already know
						return;
					} else {
						Logger.info(processorNickName + " is online");
						isProcessorOnline = true;

						// Verify that the connection is ok
						qosCheck();
					}
				}
			} else {
				/*
				 * A different processor's presence changed. Nothing related to
				 * this connection.
				 */
				Logger.debug("Ignoring presence for " + username);
			}
		}

		/**
		 * Does nothing
		 */
		@Override
		public void entriesAdded(Collection<String> arg0) {
			// don't care
		}

		/**
		 * Does nothing
		 */
		@Override
		public void entriesDeleted(Collection<String> arg0) {
			// don't care
		}

		/**
		 * Does nothing
		 */
		@Override
		public void entriesUpdated(Collection<String> arg0) {
			// don't care
		}

	}

	/**
	 * States that the XMPP client can be in.
	 * 
	 * @author Dan Mascenik
	 * 
	 */
	public enum State {
		/*
		 * All systems go
		 */
		CONNECTED,

		/*
		 * Actively trying to connect
		 */
		TRYING,

		/*
		 * Not connected and staying that way
		 */
		DISCONNECTED
	}

	public int getStatus() {
		int status = 0;
		while (true) {
			if (!isNetworkAvailable()) {
				break;
			}
			status++;

			if (processorName == null) {
				break;
			}
			status++;

			if (!conn.isConnected()) {
				break;
			}
			status++;

			if (!conn.isAuthenticated()) {
				break;
			}
			status++;

			if (conn.getRoster() == null
					|| conn.getRoster().getEntryCount() == 0) {
				break;
			}
			status++;

			if (!isProcessorOnline()) {
				break;
			}
			status++;

			if (processorChat == null) {
				break;
			}
			status++;

			break;
		}

		return status;
	}

}

/**
 * Used only by the {@link ProcessorClient}, this listens for a message produced
 * by a {@link BroadcastReceiver} listening for network status events.
 * 
 * @author Dan Mascenik
 * 
 */
interface NetworkStatusListener {

	public void onNetworkStatusChange(NetworkStatusEvent evt);

}

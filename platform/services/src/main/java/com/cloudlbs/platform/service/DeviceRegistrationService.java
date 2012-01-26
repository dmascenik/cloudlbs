package com.cloudlbs.platform.service;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudlbs.core.utils.search.Query;
import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.core.utils.service.GenericService;
import com.cloudlbs.platform.domain.Device;
import com.cloudlbs.platform.domain.SystemProperty;
import com.cloudlbs.platform.service.internal.SystemPropertyService;
import com.cloudlbs.sls.protocol.DeviceConnectionProto.DeviceConnectionMessage;

/**
 * @author Dan Mascenik
 * 
 */
@Service
public class DeviceRegistrationService implements
		GenericService<DeviceConnectionMessage> {

	@Autowired
	private GenericService<Device> deviceService;

	@Autowired
	private XmppAdminService xmppAdminService;

	@Autowired
	private XmppMessagingService xmppMessagingService;

	@Autowired
	private SystemPropertyService systemPropertyService;

	/**
	 * This looks up a device based on its unique identifier, and returns its
	 * XMPP connection details as well as the processor for it to chat with.
	 */
	@Override
	public DeviceConnectionMessage retrieveEntity(String deviceUniqueId)
			throws EntityNotFoundException {

		/*
		 * See if the device already exists
		 */
		log.debug("Looking up device " + deviceUniqueId);
		SearchResult<Device> results = deviceService.search(new Query(
				"deviceUniqueId: " + deviceUniqueId, 0, 1));
		Device device = null;
		if (results.getTotalResults() == 1) {
			log.debug("Found existing device");
			device = results.getValues().get(0);
		} else if (results.getTotalResults() == 0) {
			device = new Device();
			device.setDeviceUniqueId(deviceUniqueId);
			device.setXmppUsername(deviceUniqueId);

			String xmppPassword = UUID.randomUUID().toString();
			try {
				xmppAdminService.createAccount(deviceUniqueId, xmppPassword);
			} catch (XMPPException e) {
				throw new RuntimeException(
						"Failed to create XMPP account for device "
								+ deviceUniqueId + ". Already exists?");
			}

			device.setXmppPassword(xmppPassword);
			deviceService.createEntity(device);
			log.debug("Created new device");
		} else {
			throw new RuntimeException("Got an unexpected number of results: "
					+ results.getTotalResults());
		}

		/*
		 * Put XMPP credentials on the outbound message
		 */
		DeviceConnectionMessage.Builder dcmNew = DeviceConnectionMessage
				.newBuilder();
		dcmNew.setDeviceUniqueId(device.getDeviceUniqueId());
		dcmNew.setXmppUsername(device.getXmppUsername());
		dcmNew.setXmppPassword(device.getXmppPassword());

		/*
		 * Add external XMPP host and port
		 */
		Properties xmppProps = systemPropertyService
				.getAsProperties((SystemProperty.CATEGORY_XMPP));
		String host = xmppProps
				.getProperty(SystemProperty.KEY_XMPP_EXTERNAL_HOST);
		int port = Integer.parseInt(xmppProps
				.getProperty(SystemProperty.KEY_XMPP_EXTERNAL_PORT));
		String xmppUserSuffix = xmppProps
				.getProperty(SystemProperty.KEY_XMPP_USERNAME_SUFFIX);

		dcmNew.setXmppHost(host);
		dcmNew.setXmppPort(port);

		/*
		 * Tell the device to use this processor instance
		 * 
		 * FIXME Assumes that HTTP request load balancing accurately reflects
		 * number of XMPP connections on a processor instance
		 */

		String processorName = xmppMessagingService.getProcessorName();
		dcmNew.setProcessorName(processorName + "@" + xmppUserSuffix);
		log.debug("Directing " + deviceUniqueId + " to " + processorName + "@"
				+ xmppUserSuffix);

		/*
		 * Add the device to this processor's roster if not already there
		 */
		try {
			xmppMessagingService.addRosterEntry(device.getXmppUsername() + "@"
					+ xmppUserSuffix, device.getXmppUsername());
		} catch (XMPPException e) {
			log.error("Failed to add " + device.getXmppUsername()
					+ " to roster for " + processorName
					+ ". Presence changes for device will not be captured.", e);
		}

		return dcmNew.build();
	}

	@Override
	public DeviceConnectionMessage createEntity(DeviceConnectionMessage dcm) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public SearchResult<DeviceConnectionMessage> search(Query query) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public long count(Query query) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public void deleteEntity(String id) throws EntityNotFoundException {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public DeviceConnectionMessage updateEntity(String id,
			DeviceConnectionMessage representation,
			List<String> unmodifiedFields) throws EntityNotFoundException {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public Class<DeviceConnectionMessage> entityClass() {
		return DeviceConnectionMessage.class;
	}

	private Logger log = LoggerFactory.getLogger(getClass());
}

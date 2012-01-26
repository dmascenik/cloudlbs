package com.cloudlbs.platform.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloudlbs.core.utils.protocol.NoopMessageConverter;
import com.cloudlbs.core.utils.service.GenericService;
import com.cloudlbs.core.utils.web.GenericController;
import com.cloudlbs.sls.protocol.DeviceConnectionProto.DeviceConnectionMessage;

/**
 * This web controller only supports GET where the GUID is the unique ID of the
 * device. If the device does not exist in the system, a new record and XMPP
 * account will be created.<br/>
 * <br/>
 * Currently, it is assumed that an upstream load balancer is evenly
 * distributing calls to processor instances, so this call will always return
 * the XMPP handle of this processor instance for the device to chat with. More
 * sophisticated load balancing of XMPP connections may be required in the
 * future.
 * 
 * @author Dan Mascenik
 * 
 */
@Controller
@RequestMapping("/device/connection")
public class DeviceRegistrationController extends
		GenericController<DeviceConnectionMessage, DeviceConnectionMessage> {

	/**
	 * @param deviceConnectionService
	 */
	@Autowired
	public DeviceRegistrationController(
			GenericService<DeviceConnectionMessage> deviceRegistrationService) {
		super(deviceRegistrationService,
				new NoopMessageConverter<DeviceConnectionMessage>(
						DeviceConnectionMessage.class), null);
	}

	@Override
	public void deleteResource(String stub, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	@Override
	public void getQuery(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	@Override
	public void postResource(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	@Override
	public void postQuery(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	@Override
	public void putResource(String stub, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	/**
	 * This controller may not need authentication at all, but being public, it
	 * should complain about attempts to claim pre-authentication.
	 */
	@Override
	protected boolean isPreauthenticationAllowed() {
		return false;
	}
}

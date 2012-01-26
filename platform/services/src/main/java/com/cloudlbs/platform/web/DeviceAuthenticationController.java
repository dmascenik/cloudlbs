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
import com.cloudlbs.sls.protocol.AuthenticationProto.AuthenticationMessage;

/**
 * Handles user authentication for an app running on a particular device. POST
 * is used for login, GET for authentication details, and DELETE for logout. No
 * other HTTP methods are supported.
 * 
 * @author Dan Mascenik
 * 
 */
@Controller
@RequestMapping("/device/auth")
public class DeviceAuthenticationController extends
		GenericController<AuthenticationMessage, AuthenticationMessage> {

	/**
	 * @param deviceAuthenticationService
	 */
	@Autowired
	public DeviceAuthenticationController(
			GenericService<AuthenticationMessage> deviceAuthenticationService) {
		super(deviceAuthenticationService,
				new NoopMessageConverter<AuthenticationMessage>(
						AuthenticationMessage.class), null);
	}

	/**
	 * Query not allowed
	 */
	@Override
	public void getQuery(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	/**
	 * Query not allowed
	 */
	@Override
	public void postQuery(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	/**
	 * Update not allowed
	 */
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

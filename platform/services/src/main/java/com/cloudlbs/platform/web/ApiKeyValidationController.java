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
import com.cloudlbs.sls.protocol.AppDetailsProto.AppDetailsMessage;

/**
 * Handles backend validation of API keys being used by an app on a device. This
 * web controller only supports POST.
 * 
 * @author Dan Mascenik
 * 
 */
@Controller
@RequestMapping("/device/app")
public class ApiKeyValidationController extends
		GenericController<AppDetailsMessage, AppDetailsMessage> {

	/**
	 * @param appDetailsService
	 */
	@Autowired
	public ApiKeyValidationController(
			GenericService<AppDetailsMessage> apiKeyValidationService) {
		super(apiKeyValidationService,
				new NoopMessageConverter<AppDetailsMessage>(
						AppDetailsMessage.class), null);
	}

	@Override
	public void getResource(String stub, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
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

package com.cloudlbs.web.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.platform.protocol.SearchProto.QueryMessage;
import com.cloudlbs.web.server.remote.UserAccountRemote;
import com.cloudlbs.web.shared.dto.UserAccountDTO;

/**
 * @author Dan Mascenik
 * 
 */
public class UserAccountConfirmationServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		/*
		 * This should have been exported in the Spring configuration using
		 * org.springframework
		 * .web.context.support.ServletContextAttributeExporter
		 */
		UserAccountRemote userAccountRemote = (UserAccountRemote) getServletContext()
				.getAttribute("userAccountRemote");
		String systemBaseUrl = (String) getServletContext().getAttribute(
				"systemBaseUrl");

		String guid = req.getParameter("g");

		if (guid == null) {
			log.debug("Bad Request - No g parameter provided");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.flushBuffer();
			return;
		}

		QueryMessage.Builder b = QueryMessage.newBuilder();
		b.setQ("guid: " + guid);
		SearchResult<UserAccountDTO> result = userAccountRemote.search(b
				.build());

		if (result.getTotalResults() == 0) {
			log.debug("Bad Request - No such guid " + guid);
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.flushBuffer();
			return;
		}

		UserAccountDTO user = result.getValues().get(0);
		if (user.getStatus().equals(UserAccountDTO.STATUS_PENDING)) {
			/*
			 * Update the status to OK
			 */
			UserAccountDTO representation = new UserAccountDTO();
			representation.setVersion(user.getVersion());
			representation.setStatus(UserAccountDTO.STATUS_OK);
			userAccountRemote.update(user.getGuid(), representation);
		} else if (user.getStatus().equals(UserAccountDTO.STATUS_OK)) {
			/*
			 * Already confirmed (maybe a double-click) - do nothing
			 */
		} else {
			log.debug("Bad Request - User status is " + user.getStatus()
					+ " for guid " + guid);
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.flushBuffer();
			return;
		}

		resp.sendRedirect(systemBaseUrl);

	}

	private Logger log = LoggerFactory.getLogger(getClass());
}

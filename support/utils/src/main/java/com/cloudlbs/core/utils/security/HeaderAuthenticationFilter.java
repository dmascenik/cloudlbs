package com.cloudlbs.core.utils.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;


/**
 * Checks the HTTP headers to see if there is a user session, or a client ID
 * with a signed request and, if present, authenticates based on that. This
 * filter will be applied to all URLs.<br/>
 * <br/>
 * If {@link #ignoreFailure} is set to <code>true</code>, the security filter
 * chain will continue, possibly redirecting the user to a login page. This is
 * useful for debugging, but not recommended for production.
 * {@link #ignoreFailure} defaults to <code>false</code>, and authentication
 * failure simply returns an HTTP 401 Forbidden response. <br/>
 * <br/>
 * This class is essentially a modified version of Spring Security's
 * {@link BasicAuthenticationFilter}.
 * 
 * @see SessionAuthenticationToken
 * @see SignatureAuthenticationToken
 * 
 * @author Dan Mascenik
 * 
 */
public class HeaderAuthenticationFilter extends GenericFilterBean {

	private boolean ignoreFailure = false;

	/**
	 * Try to extract HTTP headers for either a
	 * {@link SessionAuthenticationToken} or a
	 * {@link SignatureAuthenticationToken}, and perform authentication based on
	 * those.
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;

		String sessionId = request
				.getHeader(SessionAuthenticationToken.SESSION_HEADER);
		String clientId = request
				.getHeader(SignatureAuthenticationToken.CLIENT_ID_HEADER);
		String signature = request
				.getHeader(SignatureAuthenticationToken.SIGNATURE_HEADER);

		Authentication authRequest = null;
		if (sessionId != null) {
			log.debug("Authenticating header "
					+ SessionAuthenticationToken.SESSION_HEADER + ": "
					+ sessionId);
			authRequest = new SessionAuthenticationToken(sessionId);
		} else if (clientId != null) {
			log.debug("Authenticating header "
					+ SignatureAuthenticationToken.CLIENT_ID_HEADER + ": "
					+ clientId);
			if (signature == null) {
				@SuppressWarnings("serial")
				AuthenticationException failed = new AuthenticationException(
						"Received a "
								+ SignatureAuthenticationToken.CLIENT_ID_HEADER
								+ " header without a "
								+ SignatureAuthenticationToken.SIGNATURE_HEADER
								+ " header") {
				};
				log.debug("Authentication request failed: " + failed.toString());
				SecurityContextHolder.getContext().setAuthentication(null);

				if (ignoreFailure) {
					chain.doFilter(request, response);
				} else {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					response.flushBuffer();
				}
			}

			String message = HttpRequestSignatureUtils.getStringToSign(request);

			authRequest = new SignatureAuthenticationToken(clientId, signature,
					message);
		} 

		if (authRequest != null) {
			SecurityContextHolder.getContext().setAuthentication(authRequest);
		}

		chain.doFilter(request, response);
	}

	protected boolean isIgnoreFailure() {
		return ignoreFailure;
	}

	public void setIgnoreFailure(boolean ignoreFailure) {
		this.ignoreFailure = ignoreFailure;
	}

	private Logger log = LoggerFactory
			.getLogger(HeaderAuthenticationFilter.class);

}

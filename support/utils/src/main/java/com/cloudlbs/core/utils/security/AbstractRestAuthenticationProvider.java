package com.cloudlbs.core.utils.security;

import org.springframework.security.authentication.AuthenticationProvider;

import com.cloudlbs.core.utils.remote.RemoteService;
import com.cloudlbs.core.utils.remote.RestProtobufRemoteService;
import com.google.protobuf.Message;

/**
 * Base class for {@link AuthenticationProvider}s that perform authentication by
 * making a call to a RESTful web service - in particular, a
 * {@link RestProtobufRemoteService}.
 * 
 * @author Dan Mascenik
 * 
 */
public abstract class AbstractRestAuthenticationProvider<M extends Message>
		implements AuthenticationProvider {

	protected RemoteService<M, M> remoteAuthenticationService;

	public AbstractRestAuthenticationProvider(
			RemoteService<M, M> remoteAuthenticationService) {
		this.remoteAuthenticationService = remoteAuthenticationService;
	}

}

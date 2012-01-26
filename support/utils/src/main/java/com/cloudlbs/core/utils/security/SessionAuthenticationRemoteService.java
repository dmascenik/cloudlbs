package com.cloudlbs.core.utils.security;

import com.cloudlbs.core.utils.remote.RestProtobufRemoteService;
import com.cloudlbs.platform.protocol.AuthenticationProto.SessionAuthenticationMessage;

/**
 * @author Dan Mascenik
 * 
 */
public class SessionAuthenticationRemoteService
		extends
		RestProtobufRemoteService<SessionAuthenticationMessage, SessionAuthenticationMessage> {

	public SessionAuthenticationRemoteService() {
		super(null, SessionAuthenticationMessage.items);
	}

}

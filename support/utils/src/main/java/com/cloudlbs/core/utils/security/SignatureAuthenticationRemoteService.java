package com.cloudlbs.core.utils.security;

import com.cloudlbs.core.utils.remote.RestProtobufRemoteService;
import com.cloudlbs.platform.protocol.AuthenticationProto.SignatureAuthenticationMessage;

/**
 * @author Dan Mascenik
 * 
 */
public class SignatureAuthenticationRemoteService
		extends
		RestProtobufRemoteService<SignatureAuthenticationMessage, SignatureAuthenticationMessage> {

	public SignatureAuthenticationRemoteService() {
		super(null, SignatureAuthenticationMessage.items);
	}

}

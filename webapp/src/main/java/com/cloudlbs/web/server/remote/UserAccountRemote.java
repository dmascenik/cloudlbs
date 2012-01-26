package com.cloudlbs.web.server.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudlbs.core.utils.remote.RestProtobufRemoteService;
import com.cloudlbs.platform.protocol.UserAccountProto.UserAccountMessage;
import com.cloudlbs.web.server.converter.UserAccountMessageConverter;
import com.cloudlbs.web.shared.dto.UserAccountDTO;

/**
 * @author Dan Mascenik
 * 
 */
@Service("userAccountRemote")
public class UserAccountRemote extends
		RestProtobufRemoteService<UserAccountMessage, UserAccountDTO> {

	@Autowired
	public UserAccountRemote(
			UserAccountMessageConverter userAccountMessageConverter) {
		super(userAccountMessageConverter, UserAccountMessage.items);
	}

}

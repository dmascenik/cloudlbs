package com.cloudlbs.web.server.converter;

import org.springframework.stereotype.Component;

import com.cloudlbs.core.utils.protocol.ProtobufMessageConverter;
import com.cloudlbs.platform.protocol.UserAccountProto.UserAccountMessage;
import com.cloudlbs.web.shared.dto.UserAccountDTO;

/**
 * @author Dan Mascenik
 * 
 */
@Component
public class UserAccountMessageConverter extends
		ProtobufMessageConverter<UserAccountMessage, UserAccountDTO> {

}

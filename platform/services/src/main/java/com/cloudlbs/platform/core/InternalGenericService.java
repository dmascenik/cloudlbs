package com.cloudlbs.platform.core;

import java.util.List;

import com.cloudlbs.core.utils.protocol.MessageConverter;
import com.cloudlbs.core.utils.service.GenericService;
import com.cloudlbs.platform.protocol.SearchProto.SearchResultMessage;
import com.google.protobuf.GeneratedMessage.GeneratedExtension;
import com.google.protobuf.Message;

public interface InternalGenericService<M extends Message, T> extends
		GenericService<T> {

	public MessageConverter<M, T> getMessageConverter();

	public GeneratedExtension<SearchResultMessage, List<M>> getMessageTypeExtension();

	public T updateEntity(T representation);
}

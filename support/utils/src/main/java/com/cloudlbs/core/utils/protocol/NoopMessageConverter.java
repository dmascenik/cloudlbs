package com.cloudlbs.core.utils.protocol;

import com.google.protobuf.JsonFormat;
import com.google.protobuf.Message;
import com.google.protobuf.XmlFormat;

/**
 * {@link MessageConverter} implementation that performs no conversion at all.
 * 
 * @author Dan Mascenik
 * 
 */
public class NoopMessageConverter<M extends Message> extends
		AbstractMessageConverter<M, M> implements MessageConverter<M, M> {

	/**
	 * @param messageClass
	 */
	public NoopMessageConverter(Class<M> messageClass) {
		super(messageClass);
	}

	@Override
	public M toMessage(M obj) {
		return obj;
	}

	@Override
	public String toJson(M obj) {
		return JsonFormat.printToString(obj);
	}

	@Override
	public String toXml(M obj) {
		return XmlFormat.printToString(obj);
	}

	@Override
	public M fromMessage(M message) {
		return message;
	}

}

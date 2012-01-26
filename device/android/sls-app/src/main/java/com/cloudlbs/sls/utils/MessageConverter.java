package com.cloudlbs.sls.utils;

import java.io.IOException;
import java.io.InputStream;

import com.google.protobuf.Message;

/**
 * Generic interface for converting between a Protocol Buffer object, and a
 * POJO.
 * 
 * @author Dan Mascenik
 * 
 * @param <M>
 *            A Protobuf {@link Message} type
 * @param <T>
 * 
 */
public interface MessageConverter<M extends Message, T> {

	M toMessage(T obj);

	T fromMessage(M message);

	M fromMessage(InputStream in) throws IOException;

}

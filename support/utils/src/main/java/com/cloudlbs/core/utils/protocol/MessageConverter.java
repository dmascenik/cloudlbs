package com.cloudlbs.core.utils.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.persistence.Entity;

import com.google.protobuf.Message;

/**
 * Generic interface for converting between a Protocol Buffer object, an
 * {@link Entity} object, and various other representations of the object.
 * 
 * @author Dan Mascenik
 * 
 * @param <M>
 *            A Protobuf {@link Message} type
 * @param <T>
 *            An {@link Entity} type
 */
public interface MessageConverter<M extends Message, T> {

	M toMessage(T obj);

	List<String> getUnsetMessageFields(M message);

	String toJson(T obj);

	String toJson(M message);

	String toXml(T obj);

	String toXml(M message);

	T fromMessage(M message);

	M fromMessage(InputStream in) throws IOException;

	M fromJson(Readable in) throws IOException;
}

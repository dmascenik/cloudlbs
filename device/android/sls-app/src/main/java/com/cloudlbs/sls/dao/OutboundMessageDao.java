package com.cloudlbs.sls.dao;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.protobuf.Message;

/**
 * Writes the outbound message queue to persistent storage and retrieve it from
 * persistent storage. The loaded {@link Queue} is in the same order as the
 * saved {@link Queue}.
 * 
 * @author Dan Mascenik
 * 
 */
public interface OutboundMessageDao {

	public void saveOutboundMessageQueue(
			ConcurrentLinkedQueue<Message> queue) throws IOException;
	public ConcurrentLinkedQueue<Message> loadOutboundMessageQueue() throws IOException;

}

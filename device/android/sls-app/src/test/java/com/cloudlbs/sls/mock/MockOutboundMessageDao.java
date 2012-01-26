package com.cloudlbs.sls.mock;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cloudlbs.sls.dao.OutboundMessageDao;
import com.google.protobuf.Message;

public class MockOutboundMessageDao implements OutboundMessageDao {

	private ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<Message>();
	
	@Override
	public void saveOutboundMessageQueue(ConcurrentLinkedQueue<Message> queue)
			throws IOException {
		this.queue = queue;
	}

	@Override
	public ConcurrentLinkedQueue<Message> loadOutboundMessageQueue()
			throws IOException {
		return queue;
	}

}

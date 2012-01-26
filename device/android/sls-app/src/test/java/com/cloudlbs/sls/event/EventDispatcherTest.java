package com.cloudlbs.sls.event;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.LogEvent;

/**
 * @author Dan Mascenik
 * 
 */
public class EventDispatcherTest extends Assert {

	@Before
	public void testInitialize() {
		EventDispatcher.initialize();
	}

	@Test
	public void testReInitialize() {
		EventDispatcher.initialize();
		EventDispatcher.initialize();
	}

	@Test
	public void testDispatchEventWithObjectListener() throws Exception {
		MyObjectListener l = new MyObjectListener();
		MyObjectListener otherone = new MyObjectListener();
		EventDispatcher.addListener(l);
		EventDispatcher.addListener(otherone);

		LogEvent evt = new LogEvent(0, "abc");
		EventDispatcher.dispatchEvent(evt);

		l.waitForMessage();
		otherone.waitForMessage();

		assertEquals("abc", l.message);
		assertEquals("abc", otherone.message);

		// Remove one
		EventDispatcher.removeListener(l);
		l.message = null;
		otherone.message = null;

		evt = new LogEvent(0, "def");
		EventDispatcher.dispatchEvent(evt);

		l.waitForMessage();
		otherone.waitForMessage();

		assertNull(l.message);
		assertEquals("def", otherone.message);

		EventDispatcher.removeListener(l);
	}

	@After
	public void tearDown() {
		EventDispatcher.destroy();
	}

	class MyObjectListener {

		String message = null;

		public synchronized void onLogEvent(LogEvent event) {
			message = event.getText();
			notify();
		}

		synchronized void waitForMessage() throws Exception {
			wait(100);
		}

	}

}

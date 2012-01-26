package com.cloudlbs.sls.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.LogEvent;

/**
 * @author Dan Mascenik
 * 
 */
public class LoggerTest {

	@Test
	public void testExceptionLogger() {
		try {
			Exception e0 = new Exception("this is the cause");
			throw new RuntimeException("here's an exception", e0);
		} catch (Exception e) {
			Logger.error("exception", e);
		}
	}

	public void onLogEvent(LogEvent evt) {
		// System.out.println(evt.getText());
	}

	@Before
	public void before() {
		EventDispatcher.initialize();
		EventDispatcher.addListener(this);
	}

	@After
	public void after() {
		EventDispatcher.destroy();
	}
}

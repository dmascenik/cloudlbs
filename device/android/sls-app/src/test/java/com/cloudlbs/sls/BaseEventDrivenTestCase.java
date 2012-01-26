package com.cloudlbs.sls;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.utils.Logger;

/**
 * @author Dan Mascenik
 * 
 */
public abstract class BaseEventDrivenTestCase extends Assert {

	private SystemOutLogListener logger = new SystemOutLogListener();

	protected void sysOutLogger(boolean use, int level) {
		Logger.setLogLevel(level);
		if (use) {
			EventDispatcher.addListener(logger);
		} else {
			EventDispatcher.removeListener(logger);
		}
	}

	@Before
	public void before() {
		EventDispatcher.initialize();
	}

	@After
	public void after() {
		EventDispatcher.destroy();
	}
}

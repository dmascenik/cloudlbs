package com.cloudlbs.sls.location;

import org.junit.Ignore;
import org.junit.Test;

import com.cloudlbs.sls.BaseEventDrivenTestCase;
import com.cloudlbs.sls.core.LocationRequestParams;
import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.ReadingFailedEvent;
import com.cloudlbs.sls.event.StartupEvent;
import com.cloudlbs.sls.mock.MockLocationDetectorManager;

/**
 * @author Dan Mascenik
 * 
 */
public class LocationDetectorManagerTest extends BaseEventDrivenTestCase {

	ScheduleManager schedMgr;
	LocationDetectorManager ldMgr;

	// GPS warmup time = 10ms

	// GPS max idle = 20ms

	// GPS failure retry = 30ms

	@Test
	public void testInitialState() {
		assertEquals(0, schedMgr.getSchedule().size());
		assertFalse(ldMgr.isLocationDetectorActive());
	}

	@Test
	public void testScheduleStartupNow() {
		assertFalse(ldMgr.isLocationDetectorActive());
		EventDispatcher.dispatchEvent(new ReadingRequest("abc",
				new LocationRequestParams(5)));
		assertEquals(1, schedMgr.getSchedule().size());
		assertTrue(ldMgr.isLocationDetectorActive());
	}

	@Ignore
	public synchronized void testScheduleWaitToStart() throws Exception {
		assertFalse(ldMgr.isLocationDetectorActive());
		EventDispatcher.dispatchEvent(new ReadingRequest("abc",
				new LocationRequestParams(25)));
		assertEquals(1, schedMgr.getSchedule().size());
		assertFalse(ldMgr.isLocationDetectorActive());
		Thread.sleep(50);
		assertTrue(ldMgr.isLocationDetectorActive());
	}

	@Test
	public void testShutdownAfterReading() {
		assertFalse(ldMgr.isLocationDetectorActive());
		EventDispatcher.dispatchEvent(new ReadingRequest("abc",
				new LocationRequestParams(5)));
		assertTrue(ldMgr.isLocationDetectorActive());
		EventDispatcher.dispatchEvent(new ReadingRequest("def",
				new LocationRequestParams(30)));
		assertEquals(2, schedMgr.getSchedule().size());
		assertTrue(ldMgr.isLocationDetectorActive());
		EventDispatcher.dispatchEvent(new CancelReadingRequest("abc"));
		assertEquals(1, schedMgr.getSchedule().size());
		assertFalse(ldMgr.isLocationDetectorActive());
	}

	@Ignore
	public void testShutdownWithNoReadings() {
		assertFalse(ldMgr.isLocationDetectorActive());
		EventDispatcher.dispatchEvent(new ReadingRequest("abc",
				new LocationRequestParams(5)));
		assertTrue(ldMgr.isLocationDetectorActive());
		EventDispatcher.dispatchEvent(new CancelReadingRequest("abc"));
		assertEquals(0, schedMgr.getSchedule().size());
		assertFalse(ldMgr.isLocationDetectorActive());
	}

	@Ignore
	public synchronized void testRetryAfterFailure() throws Exception {
		assertFalse(ldMgr.isLocationDetectorActive());
		EventDispatcher.dispatchEvent(new ReadingRequest("abc",
				new LocationRequestParams(5)));
		assertTrue(ldMgr.isLocationDetectorActive());
		EventDispatcher.dispatchEvent(new ReadingFailedEvent());
		assertEquals(1, schedMgr.getSchedule().size());
		assertFalse(ldMgr.isLocationDetectorActive());
		wait(50);
		assertEquals(1, schedMgr.getSchedule().size());
		assertTrue(ldMgr.isLocationDetectorActive());
	}

	@Override
	public void before() {
		super.before();
		schedMgr = new ScheduleManager();
		ldMgr = new MockLocationDetectorManager(null, 10, 20, 30);
		EventDispatcher.dispatchEvent(new StartupEvent());
	}

	@Override
	public void after() {
		schedMgr = null;
		ldMgr = null;
		super.after();
	}

}
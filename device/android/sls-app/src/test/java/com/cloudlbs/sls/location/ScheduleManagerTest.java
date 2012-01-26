package com.cloudlbs.sls.location;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cloudlbs.sls.BaseEventDrivenTestCase;
import com.cloudlbs.sls.core.LocationRequestParams;
import com.cloudlbs.sls.event.EventDispatcher;
import com.cloudlbs.sls.event.ReadingScheduleChangeEvent;
import com.cloudlbs.sls.event.ReadingScheduleChangeListener;
import com.cloudlbs.sls.event.StartupEvent;

/**
 * @author Dan Mascenik
 * 
 */
public class ScheduleManagerTest extends BaseEventDrivenTestCase {

	ScheduleChangeListener changeListener;
	ScheduleManager schedMgr;

	@Test
	public void testScheduleRequest() {
		assertEquals(0, schedMgr.getSchedule().size());
		assertEquals(0, changeListener.notificationCount);
		EventDispatcher.dispatchEvent(new ReadingRequest("abc",
				new LocationRequestParams()));
		assertEquals(1, schedMgr.getSchedule().size());
		assertEquals(1, changeListener.notificationCount);
		EventDispatcher.dispatchEvent(new ReadingRequest("abc",
				new LocationRequestParams()));
		assertEquals(2, schedMgr.getSchedule().size());
		assertEquals(2, changeListener.notificationCount);
	}

	@Test
	public void testScheduleOrder() {
		assertEquals(0, schedMgr.getSchedule().size());
		assertEquals(0, changeListener.notificationCount);
		ReadingRequest r1 = new ReadingRequest("abc",
				new LocationRequestParams(10));
		EventDispatcher.dispatchEvent(r1);
		ReadingRequest r2 = new ReadingRequest("abc",
				new LocationRequestParams());
		EventDispatcher.dispatchEvent(r2);
		assertEquals(2, schedMgr.getSchedule().size());
		assertEquals(2, changeListener.notificationCount);

		// Earlier reading time should come off the queue first
		assertEquals(r2, schedMgr.getSchedule().poll());
		assertEquals(r1, schedMgr.getSchedule().poll());
		assertEquals(0, schedMgr.getSchedule().size());
		assertEquals(4, changeListener.notificationCount);
	}

	@Test
	public void testCancelReading1() {
		EventDispatcher.dispatchEvent(new ReadingRequest("abc",
				new LocationRequestParams()));
		EventDispatcher.dispatchEvent(new ReadingRequest("abc",
				new LocationRequestParams(10)));
		EventDispatcher.dispatchEvent(new ReadingRequest("def",
				new LocationRequestParams(10)));
		assertEquals(3, schedMgr.getSchedule().size());
		assertEquals(3, changeListener.notificationCount);

		EventDispatcher.dispatchEvent(new CancelReadingRequest(
				"abc"));
		assertEquals(1, schedMgr.getSchedule().size());
		assertEquals(4, changeListener.notificationCount);
	}

	@Test
	public void testGetReading1() {
		EventDispatcher.dispatchEvent(new ReadingRequest("abc",
				new LocationRequestParams()));
		EventDispatcher.dispatchEvent(new ReadingRequest("abc",
				new LocationRequestParams(10)));
		EventDispatcher.dispatchEvent(new ReadingRequest("def",
				new LocationRequestParams(10)));
		assertEquals(3, schedMgr.getSchedule().size());
		assertEquals(3, changeListener.notificationCount);

		Collection<ReadingRequest> requests = schedMgr
				.getReadingRequests("abc");
		assertEquals(2, requests.size());
		assertEquals(3, schedMgr.getSchedule().size());
		assertEquals(3, changeListener.notificationCount);

		// Make sure they're clones
		ReadingRequest r = requests.iterator().next();
		schedMgr.getSchedule().remove(r);

		assertEquals(3, schedMgr.getSchedule().size());
		assertEquals(4, changeListener.notificationCount);
	}

	@Test
	public void testGetReading2() {
		EventDispatcher.dispatchEvent(new ReadingRequest("abc",
				new LocationRequestParams()));
		EventDispatcher.dispatchEvent(new ReadingRequest("abc",
				new LocationRequestParams(10)));
		EventDispatcher.dispatchEvent(new ReadingRequest("def",
				new LocationRequestParams(10)));
		assertEquals(3, schedMgr.getSchedule().size());
		assertEquals(3, changeListener.notificationCount);

		Collection<ReadingRequest> requests = schedMgr
				.getReadingRequests("def");
		assertEquals(1, requests.size());
		assertEquals(3, schedMgr.getSchedule().size());
		assertEquals(3, changeListener.notificationCount);

		// Make sure they're clones
		ReadingRequest r = requests.iterator().next();
		schedMgr.getSchedule().remove(r);

		assertEquals(3, schedMgr.getSchedule().size());
		assertEquals(4, changeListener.notificationCount);
	}

	@Test
	public void testGetNextReading1() {
		EventDispatcher.dispatchEvent(new ReadingRequest("abc",
				new LocationRequestParams(10)));
		ReadingRequest r1 = new ReadingRequest("abc",
				new LocationRequestParams());
		long r1Time = r1.getReadingTime();
		EventDispatcher.dispatchEvent(r1);
		EventDispatcher.dispatchEvent(new ReadingRequest("def",
				new LocationRequestParams(10)));
		assertEquals(3, schedMgr.getSchedule().size());
		assertEquals(3, changeListener.notificationCount);

		ReadingRequest r = schedMgr.getNextReadingRequests("abc");
		assertEquals(r1Time, r.getReadingTime());
		assertNotNull(r);
		assertEquals(3, schedMgr.getSchedule().size());
		assertEquals(3, changeListener.notificationCount);

		// Make sure they're clones
		schedMgr.getSchedule().remove(r);

		assertEquals(3, schedMgr.getSchedule().size());
		assertEquals(4, changeListener.notificationCount);
	}

	@Test
	public void testGetNextReading2() {
		EventDispatcher.dispatchEvent(new ReadingRequest("abc",
				new LocationRequestParams(10)));
		EventDispatcher.dispatchEvent(new ReadingRequest("abc",
				new LocationRequestParams()));
		ReadingRequest r1 = new ReadingRequest("def",
				new LocationRequestParams(10));
		long r1Time = r1.getReadingTime();
		EventDispatcher.dispatchEvent(r1);
		EventDispatcher.dispatchEvent(new ReadingRequest("def",
				new LocationRequestParams(20)));
		assertEquals(4, schedMgr.getSchedule().size());
		assertEquals(4, changeListener.notificationCount);

		ReadingRequest r = schedMgr.getNextReadingRequests("def");
		assertEquals(r1Time, r.getReadingTime());
		assertNotNull(r);
		assertEquals(4, schedMgr.getSchedule().size());
		assertEquals(4, changeListener.notificationCount);

		// Make sure they're clones
		schedMgr.getSchedule().remove(r);

		assertEquals(4, schedMgr.getSchedule().size());
		assertEquals(5, changeListener.notificationCount);
	}

	@Before
	public void before() {
		super.before();
		schedMgr = new ScheduleManager();
		changeListener = new ScheduleChangeListener();
		EventDispatcher.addListener(changeListener);
		EventDispatcher.dispatchEvent(new StartupEvent());
	}

	@After
	public void after() {
		schedMgr = null;
		changeListener = null;
		super.after();
	}

	public class ScheduleChangeListener implements
			ReadingScheduleChangeListener {

		int notificationCount = 0;

		@Override
		public void onReadingScheduleChange(ReadingScheduleChangeEvent evt) {
			notificationCount++;
		}

	}

}
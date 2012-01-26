package com.cloudlbs.sls.location;

import java.util.ArrayList;

import org.junit.Test;

import com.cloudlbs.sls.BaseEventDrivenTestCase;
import com.cloudlbs.sls.core.LocationRequestParams;

public class LocationProcessorTest extends BaseEventDrivenTestCase {

	@Test
	public void testScheduleRecurring() {
		ArrayList<ReadingRequest> recurrences = new ArrayList<ReadingRequest>();
		LocationRequestParams params = new LocationRequestParams(1000);
		params.setFrequencySeconds(1);
		params.setRecurringDurationMillis(0);
		ReadingRequest req = new ReadingRequest("abc", params);

		ScheduleManager sm = new ScheduleManager();
		LocationProcessor proc = new LocationProcessor(sm.getSchedule());

		assertEquals(0, recurrences.size());
		assertTrue(req.isRecurring());
		proc.scheduleRepeatReading(req, recurrences);
		assertEquals(1, recurrences.size());
	}

}

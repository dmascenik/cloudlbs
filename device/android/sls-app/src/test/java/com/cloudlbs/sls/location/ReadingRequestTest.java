package com.cloudlbs.sls.location;

import junit.framework.Assert;

import org.junit.Test;

import com.cloudlbs.sls.core.LocationRequestParams;

public class ReadingRequestTest extends Assert {

	@Test
	public void testRelativeTimeRequest() {
		long now = System.currentTimeMillis();
		ReadingRequest req = new ReadingRequest("abc",
				new LocationRequestParams(1000));
		assertTrue(req.getReadingTime() >= now + 1000);
	}

	@Test
	public void testClone() {
		LocationRequestParams params = new LocationRequestParams(1000);
		ReadingRequest orig = new ReadingRequest("abc", params);
		params.setFrequencySeconds(1);
		params.setMaxErrorMeters(2.0f);
		params.setMaxGpsAgeSeconds(3);
		params.setMaxWaitSeconds(4);
		params.setRecurringDurationMillis(5);
		params.setRecurrenceCount(10);
		ReadingRequest clone = orig.clone();
		assertFalse(clone == orig);
		assertFalse(clone.getParams() == params);
		assertEquals(orig.getReadingTime(), clone.getReadingTime());
		assertEquals(params.getFrequencySeconds(), clone.getParams()
				.getFrequencySeconds());
		assertEquals(params.getMaxErrorMeters(), clone.getParams()
				.getMaxErrorMeters());
		assertEquals(params.getMaxGpsAgeSeconds(), clone.getParams()
				.getMaxGpsAgeSeconds());
		assertEquals(params.getMaxWaitSeconds(), clone.getParams()
				.getMaxWaitSeconds());
		assertEquals(orig.getRepeatUntil(), clone.getRepeatUntil());
	}

	@Test
	public void testAccepts() {
		long now = System.currentTimeMillis();

		// Reading for now
		ReadingRequest req = new ReadingRequest("abc",
				new LocationRequestParams());

		// accept a perfect reading
		assertTrue(req.accepts(now, 0.0f));

		// reject insufficient accuracy within wait window
		req.getParams().setMaxWaitSeconds(10);
		req.getParams().setMaxErrorMeters(2.0f);
		assertFalse(req.accepts(now + 5, 4.0f));

		// accept outside wait window (tweaking the value of "now")
		assertTrue(req.accepts(now + 11 * 1000, now + 11 * 1000, 4.0f));

		// reject an early reading (tweaking the value of "now")
		assertFalse(req.accepts(now - 10, now - 10, 0.0f));

		// reject really old GPS
		req.getParams().setMaxGpsAgeSeconds(5);
		assertTrue(req.accepts(now - 5 * 1000 + 1, 0.0f));
		assertFalse(req.accepts(now - 5 * 1000 - 1, 0.0f));

	}
}

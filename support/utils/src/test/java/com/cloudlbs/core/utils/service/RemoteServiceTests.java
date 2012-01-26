package com.cloudlbs.core.utils.service;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cloudlbs.core.utils.search.SearchResult;
import com.cloudlbs.core.utils.test.NonConvertingFixedLocationRemoteService;
import com.cloudlbs.platform.protocol.LocationProto.FixedLocationMessage;
import com.cloudlbs.platform.protocol.ScopeProto.ScopeMessage;
import com.cloudlbs.platform.protocol.SearchProto.QueryMessage;

/**
 * @author Dan Mascenik
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/applicationContextRest.xml" })
public class RemoteServiceTests extends Assert {

	@Autowired
	NonConvertingFixedLocationRemoteService nonConvertingFixedLocationRemoteService;

	@Test
	public void getFixedLocation() {
		FixedLocationMessage floc = nonConvertingFixedLocationRemoteService
				.get("abc", "loc0");
		assertNotNull(floc);
		assertTrue(floc.toString().startsWith("guid"));
	}

	@Test
	public void createAndDeleteFixedLocation() {
		FixedLocationMessage.Builder b = FixedLocationMessage.newBuilder();
		b.setLatitude(12.5);
		b.setLongitude(14.5);
		b.setLabel("My New Location");
		ScopeMessage.Builder sb = ScopeMessage.newBuilder();
		sb.setGuid("a0b0c0");
		b.setScope(sb);

		FixedLocationMessage floc = nonConvertingFixedLocationRemoteService
				.create(b.build());
		assertNotNull(floc);
		assertTrue(floc.toString().startsWith("guid"));

		FixedLocationMessage saved = nonConvertingFixedLocationRemoteService
				.get(floc.getGuid());
		assertTrue(saved.toString().startsWith("guid"));

		// Undo
		nonConvertingFixedLocationRemoteService.delete(saved.getGuid());
	}

	@Test
	public void updateFixedLocation() {
		FixedLocationMessage floc = nonConvertingFixedLocationRemoteService
				.get("loc0");
		assertEquals("Location 0", floc.getLabel());
		long version = floc.getVersion();

		FixedLocationMessage.Builder b = FixedLocationMessage.newBuilder();
		b.setLabel("New Label");
		b.setVersion(floc.getVersion());
		floc = nonConvertingFixedLocationRemoteService
				.update("loc0", b.build());
		assertEquals("New Label", floc.getLabel());
		assertEquals(version + 1, floc.getVersion());

		// Undo
		b = FixedLocationMessage.newBuilder();
		b.setLabel("Location 0");
		b.setVersion(floc.getVersion());
		nonConvertingFixedLocationRemoteService.update("loc0", b.build());
	}

	@Test
	public void findFixedLocation() {
		QueryMessage.Builder qb = QueryMessage.newBuilder();
		// qb.setQ(""); // find everything
		SearchResult<FixedLocationMessage> results = nonConvertingFixedLocationRemoteService
				.search(qb.build());
		assertNotNull(results);
		assertEquals(4, results.getTotalResults());
		assertNotNull(results.getValues());
		List<FixedLocationMessage> values = results.getValues();
		assertEquals(4, values.size());
		assertTrue(FixedLocationMessage.class.isAssignableFrom(values.get(0)
				.getClass()));
	}
}

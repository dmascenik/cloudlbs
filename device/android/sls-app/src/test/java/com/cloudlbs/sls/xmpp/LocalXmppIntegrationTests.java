package com.cloudlbs.sls.xmpp;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.junit.Assert;
import org.junit.Test;

/**
 * These tests all require a localhost XMPP server to be running with 3 users
 * configured - testuser1, testuser2 and testuser3, all having password
 * "password". The tests will be run as testuser1. testuser2 should be online
 * and testuser3 offline.
 * 
 * @author Dan Mascenik
 * 
 */
public class LocalXmppIntegrationTests extends Assert {

	@Test
	public void testConnect() throws Exception {
		XMPPConnection conn = getConnection();
		conn.disconnect();
	}

	@Test
	public void testUpdateRoster() throws Exception {
		XMPPConnection conn = getConnection();
		ProcessorClient.setupRoster(conn, "testuser2@localhost");
		Roster roster = conn.getRoster();

		/*
		 * roster should have been reduced to just the provided user
		 */
		assertEquals(1,roster.getEntryCount());
		assertEquals("testuser2@localhost",roster.getEntries().iterator().next().getUser());

		conn.disconnect();
		
		/*
		 * Now verify that it switches to testuser3
		 */
		conn = getConnection();
		ProcessorClient.setupRoster(conn, "testuser3@localhost");
		roster = conn.getRoster();

		/*
		 * roster should have been reduced to just the provided user
		 */
		assertEquals(1,roster.getEntryCount());
		assertEquals("testuser3@localhost",roster.getEntries().iterator().next().getUser());
	}

	private static XMPPConnection getConnection() throws XMPPException {
		return ProcessorClient.connectAndLogin("localhost", "testuser1", "password",
				"unittest");
	}
}

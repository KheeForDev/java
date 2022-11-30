package com.kheefordev.junit.helper;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringHelperTest {

	StringHelper helper = new StringHelper();

	// AACD => CD, ACD => CD, CDEF => CDEF, CDAA => CDAA
	
	@Test
	public void testTruncateAInFirst2Positions_AinFirst2Positions() {
		assertEquals("CD", helper.truncateAInFirst2Positions("AACD"));
	}

	@Test
	public void testTruncateAInFirst2Positions2_AinFirstPositions() {
		assertEquals("CD", helper.truncateAInFirst2Positions("ACD"));
	}
	
	@Test
	public void testTruncateAInFirst2Positions2_AnotInPositions() {
		assertEquals("CDEF", helper.truncateAInFirst2Positions("CDEF"));
	}
	
	@Test
	public void testTruncateAInFirst2Positions2_AinLast2Positions() {
		assertEquals("CDAA", helper.truncateAInFirst2Positions("CDAA"));
	}
	
	// ABCD => False, ABAB => True, AB => True, A => False
	
	@Test
	public void testAreFirstAndLastTwoCharactersTheSame_ABCD() {
		assertFalse(helper.areFirstAndLastTwoCharactersTheSame("ABCD"));
	}
	
	@Test
	public void testAreFirstAndLastTwoCharactersTheSame_ABAB() {
		assertTrue(helper.areFirstAndLastTwoCharactersTheSame("ABAB"));
	}
	
	@Test
	public void testAreFirstAndLastTwoCharactersTheSame_AB() {
		assertTrue(helper.areFirstAndLastTwoCharactersTheSame("AB"));
	}
	
	@Test
	public void testAreFirstAndLastTwoCharactersTheSame_A() {
		assertFalse(helper.areFirstAndLastTwoCharactersTheSame("A"));
	}
}

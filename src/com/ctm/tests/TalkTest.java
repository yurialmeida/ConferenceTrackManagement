package com.ctm.tests;

import static org.junit.Assert.*;

import com.ctm.model.*;

import org.junit.Test;

public class TalkTest {
	
	private Talk talk = new Talk("Teste", 60);

	@Test
	public void testGetTitle() {
		String expected = "Teste";
		assertEquals(expected, talk.getTitle());
	}

	@Test
	public void testSetTitle() {
		talk.setTitle("Teste2");
		String expected = "Teste2";
		assertEquals(expected, talk.getTitle());
	}

	@Test
	public void testGetDuration() {
		Integer expected = 60;
		assertEquals(expected, talk.getDuration());
	}

	@Test
	public void testSetDuration() {
		talk.setDuration(30);
		Integer expected = 30;
		assertEquals(expected, talk.getDuration());
	}

}

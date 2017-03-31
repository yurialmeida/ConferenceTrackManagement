package com.ctm.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ctm.model.*;

public class TrackTest {
	
	private Track track = new Track(1);

	@Test
	public void testGetPosition() {
		Integer expected = 1;
		assertEquals(expected, this.track.getPosition());
	}

	@Test
	public void testSetPosition() {
		Integer expected = 2;
		track.setPosition(2);
		assertEquals(expected, this.track.getPosition());
	}

	@Test
	public void testGetTalks() {
		assertNotNull(this.track.getTalks());
	}

	@Test
	public void testSetTalks() {
		List<Talk> talks = new ArrayList<Talk>();
		talks.add(new Talk("Teste", 60));
		this.track.setTalks(talks);
		assertEquals(talks, this.track.getTalks());
	}

}

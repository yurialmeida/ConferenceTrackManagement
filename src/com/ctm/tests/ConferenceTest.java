package com.ctm.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import com.ctm.model.*;

import org.junit.Test;

import com.ctm.model.Conference;

public class ConferenceTest {
	
	private Conference conference = new Conference();

	@Test
	public void testGetTracks() {
		assertNull(this.conference.getTracks());
	}

	@Test
	public void testSetTracks() {
		List<Track> tracks = new ArrayList<Track>();
		tracks.add(new Track(1));
		this.conference.setTracks(tracks);
		
		assertEquals(tracks, this.conference.getTracks());
	}

}

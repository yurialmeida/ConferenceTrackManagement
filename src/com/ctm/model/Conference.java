package com.ctm.model;

import java.util.List;

public class Conference {
	
	private List<Track> tracks;

	public Conference() {}
	
	public List<Track> getTracks() {
		return tracks;
	}

	public void setTracks(List<Track> tracks) {
		this.tracks = tracks;
	}

}

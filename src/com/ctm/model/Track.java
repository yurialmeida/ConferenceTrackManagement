package com.ctm.model;

import java.util.ArrayList;
import java.util.List;

public class Track {
	
	private Integer position;
	private List<Talk> talks;
		
	public Track(Integer position) {
		this.position = position;
		this.talks = new ArrayList<Talk>();
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public List<Talk> getTalks() {
		return talks;
	}

	public void setTalks(List<Talk> talks) {
		this.talks = talks;
	}

}

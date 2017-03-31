package com.ctm.management;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.ctm.model.Conference;
import com.ctm.model.Talk;
import com.ctm.model.Track;

public final class CTM {
	
	private static Conference conference = new Conference();
	
	private static void generateTracks(List<Talk> talks) {
		
		int count = 1;
		Track track = new Track(count);
		List<Talk> trackTalks = new ArrayList<Talk>();
		List<Track> tracks = new ArrayList<Track>();
		
		int conf = 0;
		int lunch = (CTMConfiguration.LUNCH_HOUR - CTMConfiguration.OPENING_HOUR) * 60;
		int maxNetworking = (CTMConfiguration.MAX_NETWORKING_HOUR - CTMConfiguration.OPENING_HOUR) * 60;
		boolean morningSession = true;
		
		for (Talk talk : talks) {
			conf += talk.getDuration();

			// Adding lunch hour in the track
			if (conf == lunch) {
				conf += 60;
				morningSession = false;
				
				trackTalks.add(talk);
				trackTalks.add(new Talk("Lunch", 60));
				
				continue;
			}
			
			// Adding "Networking Event" in the current track and creating a new track
			if (!morningSession && conf > maxNetworking) {
				trackTalks.add(new Talk("Networking Event", 60));
				track.setTalks(trackTalks);
				tracks.add(track);
				
				count++;
				track = new Track(count);
				trackTalks = new ArrayList<Talk>();
				trackTalks.add(talk);
				morningSession = true;
				conf = talk.getDuration();
				
				continue;
			}
			
			trackTalks.add(talk);
		}
		
		// Adding "Networking Event" in the last track 
		trackTalks.add(new Talk("Networking Event", 60));
		track.setTalks(trackTalks);
		tracks.add(track);
		
		// Including tracks in the conference
		conference.setTracks(tracks);
		
		Calendar time = Calendar.getInstance();
		String mask = "H:mma";
	    SimpleDateFormat sdf = new SimpleDateFormat(mask);
		
		System.out.println();
		System.out.println("Test output:");
		
		for (Track confTrack : conference.getTracks()) {
			time.set(Calendar.HOUR_OF_DAY, CTMConfiguration.OPENING_HOUR);
			time.set(Calendar.MINUTE, 0);
			time.set(Calendar.SECOND, 0);
			
			System.out.println();
			System.out.println("Track " + confTrack.getPosition() + ":");
			System.out.println();
			
			for (Talk confTalk : confTrack.getTalks()) {
				if (confTalk.getDuration().equals(5)) {
					System.out.println(sdf.format(time.getTime()) + " " + confTalk.getTitle() + " lightning");
				} else {
					System.out.println(sdf.format(time.getTime()) + " " + confTalk.getTitle() + " " + confTalk.getDuration() + "min");
				}
				time.add(Calendar.MINUTE, confTalk.getDuration());
			}
		}
		
		System.exit(0);
	}
	
	private static boolean validateTracks(List<Talk> talks) {
		
		int conf = 0;
		int lunch = (CTMConfiguration.LUNCH_HOUR - CTMConfiguration.OPENING_HOUR) * 60;
		int minNetworking = (CTMConfiguration.MIN_NETWORKING_HOUR - CTMConfiguration.OPENING_HOUR) * 60;
		int maxNetworking = (CTMConfiguration.MAX_NETWORKING_HOUR - CTMConfiguration.OPENING_HOUR) * 60;
				
		boolean morningSession = true;
		int count = 0;
		
		for (Talk talk : talks) {
			count++;
			conf += talk.getDuration();

			// Checking if lunch hour is on time
			if (conf == lunch) {
				conf += 60;
				morningSession = false;
				continue;
			}
			
			// Checking if lunch is after due
			if (morningSession && conf > lunch) {
				return false;
			}
			
			// Checking talks time
			if (!morningSession && conf > maxNetworking) {
				conf -= talk.getDuration();
				
				if (conf >= minNetworking && conf <= maxNetworking) {
					morningSession = true;
					conf = 0;
					conf += talk.getDuration();
					continue;
				} else {
					return false;
				}
			}
			
			if (conf < minNetworking && talks.size() == count) {
				return false;
			}
		}
		
		return true;
	}

	private static void permuteTalks(List<Talk> talks, int n) {
		
        if (n == talks.size()) {
        	// Checking if the generated order is valid
        	if (validateTracks(talks)) {
        		// Creating objects and printing it
        		generateTracks(talks);
        	}
        } else {
            for (int i = n; i < talks.size(); i++) {
                Talk temp = talks.get(n);
                talks.set(n, talks.get(i));
                talks.set(i, temp);
 
                permuteTalks(talks, n + 1);
 
                temp = talks.get(n);
                talks.set(n, talks.get(i));
                talks.set(i, temp);
            }
        }
	}

	private static List<Talk> processTalksFromInputFile(String inputFileName) throws IOException {
		
        List<Talk> talks = new ArrayList<Talk>();
        FileInputStream fstream = null;
        
        // Opening file
        try{
          fstream = new FileInputStream(inputFileName);
        } catch (FileNotFoundException ex) {
        	System.err.println("Erro while opening the file.");
        	System.err.println(ex.getMessage());
        }
        
        // Reading file and processing talks
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
        
        System.out.println("Test input:");
        System.out.println();
        while ((strLine = br.readLine()) != null) {
        	if(strLine.isEmpty()) {
                continue;
        	}
        	
        	System.out.println(strLine);
        	
        	String title = strLine.substring(0, strLine.lastIndexOf(" "));
        	if(title == null || title.trim().equals("")) {
        		System.out.println("Skipping talk in the list: Untitled talk. Talks must have a title.");
                continue;
        	}

            String duration = strLine.substring(strLine.lastIndexOf(" ") + 1);
            Talk talk;
            
            if (duration.equals("lightning")) {
                talk = new Talk(title, 5);
            } else if (duration.endsWith("min")) {
                Integer minutes = Integer.parseInt(duration.replaceAll("min", ""));
                talk = new Talk(title, minutes);
            } else {
            	System.out.println("Skipping talk in the list: Time not specified. Talks must have a duration.");
            	continue;
            }

            // Adding the processed talk to the list
        	talks.add(talk);
        }
        
        try {
            br.close();
        } catch (IOException ex) {
        	System.err.println("Error: " + ex.getMessage());
        }
        
        return talks;   
    }
	
	public static void main(String[] args) {

		List<Talk> talks = new ArrayList<Talk>();
		try {
			talks = processTalksFromInputFile(CTMConfiguration.INPUT_FILE);
		} catch (IOException ex) {
			System.err.println("Error: " + ex.getMessage());
		}
		
		Integer totalTime = 0;
		for (Talk talk : talks) {
			totalTime += talk.getDuration();
		}
		
		Integer minDuration = (CTMConfiguration.MIN_NETWORKING_HOUR - CTMConfiguration.OPENING_HOUR - 1) * 60;
		
		// Checking if there is enough talk time to create a conference
		if (totalTime < minDuration) {
			System.out.println("It's impossible to generate a Conference: Not enough talk time.");
			System.exit(0);
		}
		
		// Arranging talks and printing valid tracks
		permuteTalks(talks, 0);

	}

}

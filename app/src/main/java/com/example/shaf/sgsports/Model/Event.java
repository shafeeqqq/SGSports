package com.example.shaf.sgsports.Model;

import com.example.shaf.sgsports.SkillLevel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Event {

    private String eventID;
    private String name;
    private Facility facility;
    private String organiser;   // userID string
    private Date dateCreated;
    private Date dateOfEvent;
    private String fromTime;
    private String toTime;
    private String sportsCategory;
    private int maxParticipants;
    private String description;
    private String skillLevel;
    private Map<String,Request> requests;

    public Event() {

    }

    public Event(String eventID, String name, Facility facility, String organiser, Date dateCreated,
                 Date dateOfEvent, String fromTime, String toTime,
                 String sportsCategory, int maxParticipants, String description,
                 String skillLevel) {

        this.eventID = eventID;
        this.name = name;
        this.facility = facility;
        this.organiser = organiser;
        this.dateCreated = dateCreated;
        this.dateOfEvent = dateOfEvent;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.sportsCategory = sportsCategory;
        this.maxParticipants = maxParticipants;
        this.description = description;
        this.skillLevel = skillLevel;
    }

    public Event(String eventID, String name, String organiser, Date dateCreated,
                 Date dateOfEvent, String fromTime, String toTime,
                 String sportsCategory, int maxParticipants, String description,
                 String skillLevel) {
        this.eventID = eventID;
        this.name = name;
        this.organiser = organiser;
        this.dateCreated = dateCreated;
        this.dateOfEvent = dateOfEvent;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.sportsCategory = sportsCategory;
        this.maxParticipants = maxParticipants;
        this.description = description;
        this.skillLevel = skillLevel;
        this.requests = null;
    }

    public String getEventID() {
        return eventID;
    }

    public String getName() {
        return name;
    }

    public Map<String, Request> getRequests() {
        return requests;
    }

    public Facility getFacility() {
        return facility;
    }

    public String getOrganiser() {
        return organiser;
    }

    public String getFromTime() {
        return fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateOfEvent() {
        return dateOfEvent;
    }

    public String getSportsCategory() {
        return sportsCategory;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public String getDescription() {
        return description;
    }

    public String getSkillLevel() {
        return skillLevel;
    }


    public int getVacancy() {
        int participants = 0;

        if (requests == null || requests.size() == 0)
            return  maxParticipants-1;

        else {
            for (Request req: requests.values()) {
                if (req.isAccepted())
                    participants += 1;
            }
        }
        return maxParticipants - participants -1;
    }


    public String dateCreatedText() {
        SimpleDateFormat getDate = new SimpleDateFormat("dd MMM, yyyy");
        return getDate.format(dateOfEvent);
    }

    public String timeText() {
        return fromTime + " - " + toTime;
    }
}

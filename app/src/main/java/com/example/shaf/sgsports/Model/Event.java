package com.example.shaf.sgsports.Model;

import com.example.shaf.sgsports.SkillLevel;

import java.util.Date;

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
    private SkillLevel skillLevel;

    public Event() {

    }

    public Event(String eventID, String name, Facility facility, String organiser, Date dateCreated,
                 Date dateOfEvent, String fromTime, String toTime,
                 String sportsCategory, int maxParticipants, String description,
                 SkillLevel skillLevel) {
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
                 SkillLevel skillLevel) {
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
    }

    public String getEventID() {
        return eventID;
    }

    public String getName() {
        return name;
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

    public SkillLevel getSkillLevel() {
        return skillLevel;
    }
}

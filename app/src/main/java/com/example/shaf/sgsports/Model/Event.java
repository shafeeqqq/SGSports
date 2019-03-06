package com.example.shaf.sgsports.Model;

import java.util.Date;

public class Event {

    private String eventID;
    private String name;
    private Facility facility;
    private String organiser;   // userID string
    private Date dateCreated;
    private Date dateOfEvent;
    private Sport sportsCategory;
    private int maxParticipants;
    private String description;


    public String getName() {
        return name;
    }

    public Facility getFacility() {
        return facility;
    }

    public Date getDateOfEvent() {
        return dateOfEvent;
    }

    public Sport getSportsCategory() {
        return sportsCategory;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public String getDescription() {
        return description;
    }


}

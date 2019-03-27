package com.example.shaf.sgsports.Model;

public class UserInEvents {

    private String eventID;
    private String userID;
    private String eventName;

    public UserInEvents() {}

    public UserInEvents(String eventID, String userID, String eventName) {
        this.eventID = eventID;
        this.userID = userID;
        this.eventName = eventName;
    }

    public String getEventID() {
        return eventID;
    }

    public String getUserID() {
        return userID;
    }

    public String getEventName() {
        return eventName;
    }
}

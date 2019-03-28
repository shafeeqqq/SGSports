package com.example.shaf.sgsports.Model;

import com.google.firebase.database.Exclude;

import java.util.Date;

public class Request {

    public static final String ACCEPTED = "accepted";
    public static final String PENDING = "pending";
    public static final String REJECTED = "rejected";

    private String id;
    private String userID;
    private String requesterName;
    private Date timeStamp;
    private String status = PENDING;       // accepted pending or reject

    public String getUserName() {
        return requesterName;
    }

    public Request() {

    }

    public Request(String id, String userID, String requesterName, Date timeStamp, String status) {
        this.id = id;
        this.userID = userID;
        this.requesterName = requesterName;
        this.timeStamp = timeStamp;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getUserID() {
        return userID;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getStatus() {
        return status;
    }

    @Exclude
    public boolean isAccepted() {
        return status.equals(ACCEPTED);
    }
}

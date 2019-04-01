package com.example.shaf.sgsports.Model;

import java.io.Serializable;
import java.util.Date;

public class Request implements Serializable {

    public static final String ACCEPTED = "accepted";
    public static final String PENDING = "pending";
    public static final String REJECTED = "rejected";

    private String id;
    private String userID;
    private String requesterName;
    private String requesterIcon;
    private Date timeStamp;
    private String status = PENDING;       // accepted pending or reject


    public Request() {

    }

    public String getId() {
        return id;
    }

    public String getUserID() {
        return userID;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getRequesterIcon() {
        return requesterIcon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Request(String id, String userID, String requesterName, String requesterIcon, Date timeStamp, String status) {
        this.id = id;
        this.userID = userID;
        this.requesterName = requesterName;
        this.requesterIcon = requesterIcon;
        this.timeStamp = timeStamp;
        this.status = status;
    }


    @com.google.firebase.firestore.Exclude
    public boolean isAccepted() {
        return status.equals(ACCEPTED);
    }

    @com.google.firebase.firestore.Exclude
    public boolean isPending() {
        return status.equals(PENDING);
    }
}

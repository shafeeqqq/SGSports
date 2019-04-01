package com.example.shaf.sgsports.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.shaf.sgsports.Utils.ManageRequestAdapter.ACCEPT;
import static com.example.shaf.sgsports.Utils.ManageRequestAdapter.DECLINE;

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
    private ArrayList<Request> requests;

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
        this.requests = new ArrayList<Request>();
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
        this.requests = new ArrayList<Request>();;
    }

    public String getEventID() {
        return eventID;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Request> getRequests() {
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


    @com.google.firebase.firestore.Exclude
    public int getVacancy() {
        int participants = 0;

        if (requests == null || requests.size() == 0)
            return  maxParticipants-1;

        else {
            for (Request req: requests) {
                if (req.isAccepted())
                    participants += 1;
            }
        }
        return maxParticipants - participants -1;
    }

    @com.google.firebase.firestore.Exclude
    public String dateCreatedText() {
        SimpleDateFormat getDate = new SimpleDateFormat("dd MMM, yyyy");
        if (dateOfEvent==null)
            return "Unknown";
        return getDate.format(dateOfEvent);
    }

    public String timeText() {
        return fromTime + " - " + toTime;
    }

    @com.google.firebase.firestore.Exclude
    public String getRequestStatus(String userID) {
        for (Request item: requests) {
            if (item.getUserID().equals(userID))
                return item.getStatus();
        }
        return "invalid";
    }

    @com.google.firebase.firestore.Exclude
    public String hasRequestFrom(String userID) {
        for (Request item: requests) {
            if (item.getUserID().equals(userID))
                return item.getStatus();
        }
        return "NO";
    }

    public void updateRequestStatus(String requestID, int flag) {
        for (Request item: requests) {
            if (item.getId().equals(requestID)) {
                if (flag == ACCEPT)
                    item.setStatus(Request.ACCEPTED);
                else if (flag == DECLINE)
                    item.setStatus(Request.REJECTED);
            }
        }
    }

    @com.google.firebase.firestore.Exclude
    public ArrayList<Request> queryPendingRequests() {
        ArrayList<Request> result = new ArrayList<Request>();

        for (Request req: requests) {
            if (req.isPending())
                result.add(req);
        }

        return result;
    }

    @com.google.firebase.firestore.Exclude
    public boolean isPast() {
        return dateOfEvent.before(new Date());
    }

    @com.google.firebase.firestore.Exclude
    public String getAddress() {
        if (facility == null || facility.getAddressText().isEmpty())
            return "Unknown";
        return facility.getAddressText();

    }
}

package com.example.shaf.sgsports.Model;

import java.util.ArrayList;

public class Facility {

    private String facilityID;
    private String name;
    private String addressText;
    private String direction;
    private String imageUrl;
    private String operatingHours;
    private ArrayList<String> facilityInformation;


    public Facility(String facilityID, String name, String address, ArrayList<Request> facilityInformation) {
        this.facilityID = facilityID;
        this.name = name;
        this.addressText = address;
        this.direction = direction;
    }

    public Facility() {}

    public String getFacilityID() {
        return facilityID;
    }

    public String getName() {
        return name;
    }

}

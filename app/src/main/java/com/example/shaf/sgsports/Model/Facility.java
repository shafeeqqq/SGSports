package com.example.shaf.sgsports.Model;

import java.util.ArrayList;

public class Facility {

    private String facilityID;
    private String name;
    private String address;
    private ArrayList<Sport> availSports;


    public Facility(String facilityID, String name, String address, ArrayList<Sport> availSports) {
        this.facilityID = facilityID;
        this.name = name;
        this.address = address;
        this.availSports = availSports;
    }

    public Facility() {}

    public String getFacilityID() {
        return facilityID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public ArrayList<Sport> getAvailSports() {
        return availSports;
    }
}

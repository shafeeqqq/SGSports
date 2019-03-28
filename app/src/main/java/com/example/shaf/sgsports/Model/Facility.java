package com.example.shaf.sgsports.Model;

import java.util.ArrayList;

public class Facility {

    private String facilityID;
    private String name;
    private String addressText;
    private String phone;
    private String imageUrl;
    private String operatingHours;
    private String facilityInformation;

    public Facility(String facilityID, String name, String addressText, String phone, String imageUrl, String operatingHours, String facilityInformation) {
        this.facilityID = facilityID;
        this.name = name;
        this.addressText = addressText;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.operatingHours = operatingHours;
        this.facilityInformation = facilityInformation;
    }

    public String getAddressText() {
        return addressText;
    }

    public String getPhone() {
        return phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public String getFacilityInformation() {
        return facilityInformation;
    }

    public Facility() {}

    public String getFacilityID() {
        return facilityID;
    }

    public String getName() {
        return name;
    }

}

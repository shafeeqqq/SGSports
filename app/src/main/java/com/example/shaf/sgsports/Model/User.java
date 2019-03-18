package com.example.shaf.sgsports.Model;

import java.util.Date;
import java.util.HashMap;

public class User {

    private String name;
    private String userID;
    private Date dateOfBirth;
    private float rating;
    private Date joinDate;
    private HashMap<String, String> interests;
    private String imageURL;



    public User(String name, String userID, Date dateOfBirth, float rating, Date joinDate, HashMap<String, String> interests, String imageURL) {
        this.name = name;
        this.userID = userID;
        this.dateOfBirth = dateOfBirth;
        this.rating = rating;
        this.joinDate = joinDate;
        this.interests = interests;
        this.imageURL = imageURL;
    }

    public User() {}

    public String getName() {
        return name;
    }

    public String getUserID() {
        return userID;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public float getRating() {
        return rating;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public HashMap<String, String> getInterests() {
        return interests;
    }

    public String getImageURL() {
        return imageURL;
    }


    public int getAge() {
        return 100000;
    }

}

package com.example.shaf.sgsports.Model;

import java.util.Date;
import java.util.HashMap;

public class User {

    private String name;
    private String userID;
    private String aboutMe;
    private String gender;
    private Date dateOfBirth;
    private float rating;
    private Date joinDate;
    private HashMap<String, String> interests;
    private String imageURL;

    public User(String userID, String name, String aboutMe, String gender, Date dateOfBirth, float rating, Date joinDate, HashMap<String, String> interests, String imageURL) {
        this.name = name;
        this.userID = userID;
        this.aboutMe = aboutMe;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.rating = rating;
        this.joinDate = joinDate;
        this.interests = interests;
        this.imageURL = imageURL;
    }

    public User(String userID, String name, Date joinDate, String imageURL) {
        this.name = name;
        this.userID = userID;
        this.joinDate = joinDate;
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

    public int age() {
        return 100000;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public String getGender() {
        return gender;
    }

    @com.google.firebase.firestore.Exclude
    public void setName(String name) {
        this.name = name;
    }

    @com.google.firebase.firestore.Exclude
    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    @com.google.firebase.firestore.Exclude
    public void setGender(String gender) {
        this.gender = gender;
    }

    @com.google.firebase.firestore.Exclude
    public void setInterests(HashMap<String, String> interests) {
        this.interests = interests;
    }

    @com.google.firebase.firestore.Exclude
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}

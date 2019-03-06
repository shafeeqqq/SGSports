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


    public int getAge() {
        return 100000;
    }

}

package com.example.tour.Data;

import android.widget.EditText;

public class MainData {
    private String uid;
    private String name;
    private String email;
    private String password;
    private double cellNumber;

    private String tourName, tourDescription;
    private long startDate, endDate;
    private float budget;


    public MainData() {
    }

    public MainData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public MainData(String uid, String name, String email, String password, double cellNumber) {
        this.uid=uid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.cellNumber = cellNumber;
    }

    public MainData(String uid, String tourName, String tourDescription, long startDate, long endDate, float budget) {
        this.uid = uid;
        this.tourName = tourName;
        this.tourDescription = tourDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
    }



    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getCellNumber() {
        return cellNumber;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTourName() {
        return tourName;
    }

    public String getTourDescription() {
        return tourDescription;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public float getBudget() {
        return budget;
    }
}

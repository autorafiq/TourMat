package com.example.tour.Data;

import android.widget.EditText;

public class MainData {
    private String uid;
    private String name;
    private String email;
    private String password;
    private double cellNumber;


    public MainData() {
    }

    public MainData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public MainData(String uid, String name, String email, double cellNumber) {
        this.uid=uid;
        this.name = name;
        this.email = email;
        this.cellNumber = cellNumber;
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


}

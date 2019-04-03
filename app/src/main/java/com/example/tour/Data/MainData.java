package com.example.tour.Data;

public class MainData {
    private String name;
    private String email;
    private String password;
    private Double cellNumber;

    public MainData() {
    }


    public MainData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public MainData(String name, String email, String password, Double cellNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
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

    public Double getCellNumber() {
        return cellNumber;
    }
}

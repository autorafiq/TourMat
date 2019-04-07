package com.example.tour.Data;

public class Data {
    private String tourName, tourDescription;
    private long startDate, endDate;
    private double budget;
    private String tourUid;

    public Data() {
    }

    public Data(String tourName, String tourDescription, long startDate, long endDate, double budget) {
        this.tourName = tourName;
        this.tourDescription = tourDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
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

    public double getBudget() {
        return budget;
    }

    public String getTourUid() {
        return tourUid;
    }

    public void setTourUid(String tourUid) {
        this.tourUid = tourUid;
    }
}

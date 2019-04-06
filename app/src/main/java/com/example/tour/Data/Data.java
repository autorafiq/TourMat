package com.example.tour.Data;

public class Data {
    private String tourName, tourDescription;
    private long startDate, endDate;
    private String budget;
    private String tourUid;

    public Data() {
    }

    public Data(String tourUid, String tourName, String tourDescription, long startDate, long endDate, String budget) {
        this.tourUid = tourUid;
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

    public String getBudget() {
        return budget;
    }

    public void setTourUid(String tourUid) {
        this.tourUid = tourUid;
    }
}

package com.example.tour.Data;

public class TourExpense {
    private String expenseDescription;
    private double tourCost;
    private String costId;

    public TourExpense() {
    }

    public TourExpense(String expenseDescription, double tourCost) {
        this.expenseDescription = expenseDescription;
        this.tourCost = tourCost;
    }

    public String getExpenseDescription() {
        return expenseDescription;
    }

    public double getTourCost() {
        return tourCost;
    }

    public String getCostId() {
        return costId;
    }

    public void setCostId(String costId) {
        this.costId = costId;
    }
}

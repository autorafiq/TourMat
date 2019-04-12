package com.example.tour.Data;

public class TourExpense {
    private String expenseDescription;
    private String tourCost;
    private String costId;

    public TourExpense() {
    }

    public TourExpense(String expenseDescription, String tourCost) {
        this.expenseDescription = expenseDescription;
        this.tourCost = tourCost;
    }

    public String getExpenseDescription() {
        return expenseDescription;
    }

    public String getTourCost() {
        return tourCost;
    }

    public String getCostId() {
        return costId;
    }

    public void setCostId(String costId) {
        this.costId = costId;
    }
}

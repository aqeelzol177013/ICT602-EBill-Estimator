package com.example.individual_assingment01;

public class Bill {
    private String month;
    private double units;
    private double rebate;
    private double totalCharge;
    private double finalCost;

    public Bill(String month, double units, double rebate, double totalCharge, double finalCost) {
        this.month = month;
        this.units = units;
        this.rebate = rebate;
        this.totalCharge = totalCharge;
        this.finalCost = finalCost;
    }

    @Override
    public String toString() {
        return month + ": $" + String.format("%.2f", finalCost);
    }

}
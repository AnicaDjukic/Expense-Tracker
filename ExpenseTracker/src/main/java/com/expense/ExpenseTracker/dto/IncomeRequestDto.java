package com.expense.ExpenseTracker.dto;

public class IncomeRequestDto {

    private String description;

    private double amount;

    public IncomeRequestDto() {
    }

    public IncomeRequestDto(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }
}

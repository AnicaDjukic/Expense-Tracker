package com.expense.ExpenseTracker.dto;

public class NewExpenseRequestDto {

    private String description;

    private double amount;

    public NewExpenseRequestDto() {
    }

    public NewExpenseRequestDto(String description, double amount) {
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

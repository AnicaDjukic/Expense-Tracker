package com.expense.ExpenseTracker.dto;

import javax.validation.constraints.NotBlank;

public class IncomeRequestDto {

    @NotBlank
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

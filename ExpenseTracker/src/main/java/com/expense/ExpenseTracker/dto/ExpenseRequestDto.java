package com.expense.ExpenseTracker.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ExpenseRequestDto {

    @NotBlank
    private String description;

    private double amount;

    public ExpenseRequestDto() {
    }

    public ExpenseRequestDto(String description, double amount) {
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

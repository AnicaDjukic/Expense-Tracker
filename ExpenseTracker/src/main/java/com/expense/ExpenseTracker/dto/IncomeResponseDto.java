package com.expense.ExpenseTracker.dto;

public class IncomeResponseDto {

    private Long id;

    private String description;

    private double amount;

    public IncomeResponseDto() {
    }

    public IncomeResponseDto(Long id, String description, double amount) {
        this.id = id;
        this.description = description;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }
}

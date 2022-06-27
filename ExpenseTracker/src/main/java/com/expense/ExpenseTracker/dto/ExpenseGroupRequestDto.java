package com.expense.ExpenseTracker.dto;

public class ExpenseGroupRequestDto {

    private String name;

    private String description;

    public ExpenseGroupRequestDto() {
    }

    public ExpenseGroupRequestDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

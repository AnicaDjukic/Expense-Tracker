package com.expense.ExpenseTracker.dto;

import javax.validation.constraints.NotBlank;

public class ExpenseGroupRequestDto {

    @NotBlank
    private String name;

    @NotBlank
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

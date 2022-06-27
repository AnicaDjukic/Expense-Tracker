package com.expense.ExpenseTracker.dto;

import javax.validation.constraints.NotBlank;

public class IncomeGroupRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    public IncomeGroupRequestDto() {
    }

    public IncomeGroupRequestDto(String name, String description) {
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

package com.expense.ExpenseTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class ExpenseGroupRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

}

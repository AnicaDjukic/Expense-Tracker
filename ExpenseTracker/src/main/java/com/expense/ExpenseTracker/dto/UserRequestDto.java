package com.expense.ExpenseTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class UserRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}

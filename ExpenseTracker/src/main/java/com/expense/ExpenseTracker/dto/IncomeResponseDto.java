package com.expense.ExpenseTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class IncomeResponseDto {

    private UUID id;

    private String description;

    private double amount;
}

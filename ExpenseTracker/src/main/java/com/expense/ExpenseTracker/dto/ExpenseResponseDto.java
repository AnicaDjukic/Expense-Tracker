package com.expense.ExpenseTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ExpenseResponseDto {

    private UUID id;

    private String description;

    private double amount;
}

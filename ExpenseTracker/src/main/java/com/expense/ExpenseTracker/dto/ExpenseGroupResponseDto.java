package com.expense.ExpenseTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ExpenseGroupResponseDto {

    private UUID id;

    private String name;

    private String description;
}

package com.expense.ExpenseTracker.dto;

import com.expense.ExpenseTracker.model.ExpenseGroup;
import com.expense.ExpenseTracker.model.IncomeGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IncomeResponseDto {

    private UUID id;

    private String description;

    private double amount;

    private IncomeGroup incomeGroup;
}

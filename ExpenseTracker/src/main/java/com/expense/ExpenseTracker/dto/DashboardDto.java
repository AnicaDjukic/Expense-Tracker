package com.expense.ExpenseTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDto {

    private double totalAmount;

    private List<ExpenseResponseDto> lastFiveExpenses = new ArrayList<>();

    private List<IncomeResponseDto> lastFiveIncomes = new ArrayList<>();
}

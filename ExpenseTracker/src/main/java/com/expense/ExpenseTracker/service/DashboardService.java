package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.model.Income;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class DashboardService {

    private final ExpenseService expenseService;

    private final IncomeService incomeService;

    public DashboardService(ExpenseService expenseService, IncomeService incomeService) {
        this.expenseService = expenseService;
        this.incomeService = incomeService;
    }

    public double getTotalAmount(UUID userId) {
        double expenseAmount = calculateExpenseAmount(userId);
        double incomeAmount = calculateIncomeAmount(userId);
        return incomeAmount - expenseAmount;
    }

    public List<Expense> getLastFewExpenses(int size, UUID userId) {
        return expenseService.getLastFew(size, userId);
    }

    public List<Income> getLastFewIncomes(int size, UUID userId) {
       return incomeService.getLastFew(size, userId);
    }

    private double calculateExpenseAmount(UUID userId) {
        double amount = 0.0;
        for(Expense expense : expenseService.getAll(userId))
            amount += expense.getAmount();
        return amount;
    }

    private double calculateIncomeAmount(UUID userId) {
        double amount = 0.0;
        for(Income income : incomeService.getAll(userId))
            amount += income.getAmount();
        return amount;
    }

}

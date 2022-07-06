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

    public double getTotalAmount(String username) {
        double expenseAmount = calculateExpenseAmount(username);
        double incomeAmount = calculateIncomeAmount(username);
        return incomeAmount - expenseAmount;
    }

    public List<Expense> getLastFewExpenses(int size, String username) {
        return expenseService.getLastFew(size, username);
    }

    public List<Income> getLastFewIncomes(int size, String username) {
       return incomeService.getLastFew(size, username);
    }

    private double calculateExpenseAmount(String username) {
        double amount = 0.0;
        for(Expense expense : expenseService.getAll(username))
            amount += expense.getAmount();
        return amount;
    }

    private double calculateIncomeAmount(String username) {
        double amount = 0.0;
        for(Income income : incomeService.getAll(username))
            amount += income.getAmount();
        return amount;
    }

}

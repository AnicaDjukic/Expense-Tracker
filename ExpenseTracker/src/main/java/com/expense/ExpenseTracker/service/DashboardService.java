package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.model.Income;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DashboardService {

    private final ExpenseService expenseService;

    private final IncomeService incomeService;

    public DashboardService(ExpenseService expenseService, IncomeService incomeService) {
        this.expenseService = expenseService;
        this.incomeService = incomeService;
    }

    public double getTotalAmount() {
        double expenseAmount = calculateExpenseAmount();
        double incomeAmount = calculateIncomeAmount();
        return incomeAmount - expenseAmount;
    }

    public List<Expense> getLastFewExpenses(int size) {
        return expenseService.getLastFew(size);
    }

    public List<Income> getLastFewIncomes(int size) {
       return incomeService.getLastFew(size);
    }

    private double calculateExpenseAmount() {
        double amount = 0.0;
        for(Expense expense : expenseService.getAll())
            amount += expense.getAmount();
        return amount;
    }

    private double calculateIncomeAmount() {
        double amount = 0.0;
        for(Income income : incomeService.getAll())
            amount += income.getAmount();
        return amount;
    }

}

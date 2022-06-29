package com.expense.ExpenseTracker.repository;

import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.model.ExpenseGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    List<Expense> findByExpenseGroupOrderByCreationTimeDesc(ExpenseGroup expenseGroup);
}

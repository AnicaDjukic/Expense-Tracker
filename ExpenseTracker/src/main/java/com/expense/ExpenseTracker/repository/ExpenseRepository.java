package com.expense.ExpenseTracker.repository;

import com.expense.ExpenseTracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID>{
}

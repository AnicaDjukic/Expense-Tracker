package com.expense.ExpenseTracker.repository;

import com.expense.ExpenseTracker.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IncomeRepository extends JpaRepository<Income, UUID> {
}

package com.expense.ExpenseTracker.repository;

import com.expense.ExpenseTracker.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeRepository extends JpaRepository<Income, Long> {
}

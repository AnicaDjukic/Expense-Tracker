package com.expense.ExpenseTracker.repository;

import com.expense.ExpenseTracker.model.IncomeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeGroupRepository extends JpaRepository<IncomeGroup, Long> {
}

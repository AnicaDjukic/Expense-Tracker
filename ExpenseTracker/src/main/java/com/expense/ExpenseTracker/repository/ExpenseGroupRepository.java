package com.expense.ExpenseTracker.repository;

import com.expense.ExpenseTracker.model.ExpenseGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseGroupRepository extends JpaRepository<ExpenseGroup, Long> {
}

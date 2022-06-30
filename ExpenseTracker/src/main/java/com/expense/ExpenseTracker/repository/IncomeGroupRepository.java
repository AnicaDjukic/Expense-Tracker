package com.expense.ExpenseTracker.repository;

import com.expense.ExpenseTracker.model.IncomeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IncomeGroupRepository extends JpaRepository<IncomeGroup, UUID> {
    Optional<IncomeGroup> findByName(String name);
}

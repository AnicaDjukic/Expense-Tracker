package com.expense.ExpenseTracker.repository;

import com.expense.ExpenseTracker.model.ExpenseGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExpenseGroupRepository extends JpaRepository<ExpenseGroup, UUID> {
    Optional<ExpenseGroup> findByName(String name);
}

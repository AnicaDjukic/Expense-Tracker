package com.expense.ExpenseTracker.repository;

import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID>, QuerydslPredicateExecutor<Expense> {
    Page<Expense> findByUser(User user, PageRequest creationTime);

    List<Expense> findByUser(User user);

    Optional<Expense> findByIdAndUser(UUID id, User user);
}

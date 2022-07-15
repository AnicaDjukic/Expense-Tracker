package com.expense.ExpenseTracker.repository;

import com.expense.ExpenseTracker.model.ExpenseGroup;
import com.expense.ExpenseTracker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExpenseGroupRepository extends JpaRepository<ExpenseGroup, UUID> {

    Optional<ExpenseGroup> findByNameAndUser(String name, User user);

    Page<ExpenseGroup> findByUser(User user, PageRequest of);

    Optional<ExpenseGroup> findByIdAndUser(UUID expenseGroupId, User user);
}

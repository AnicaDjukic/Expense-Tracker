package com.expense.ExpenseTracker.repository;

import com.expense.ExpenseTracker.model.Income;
import com.expense.ExpenseTracker.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IncomeRepository extends JpaRepository<Income, UUID> {
    Page<Income> findByUser(User user, PageRequest creationTime);

    Optional<Income> findByIdAndUser(UUID id, User user);
}

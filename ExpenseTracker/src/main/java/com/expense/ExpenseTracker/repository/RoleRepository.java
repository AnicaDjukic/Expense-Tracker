package com.expense.ExpenseTracker.repository;

import com.expense.ExpenseTracker.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}

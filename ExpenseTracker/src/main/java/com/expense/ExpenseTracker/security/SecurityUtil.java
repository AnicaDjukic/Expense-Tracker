package com.expense.ExpenseTracker.security;

import com.expense.ExpenseTracker.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static User getLoggedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

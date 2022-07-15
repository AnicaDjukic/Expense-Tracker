package com.expense.ExpenseTracker.security;

import com.expense.ExpenseTracker.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Authentication getLoggedIn() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Scheduler {

    private final UserService userService;

    private final ExpenseService expenseService;

    private final IncomeService incomeService;

    public Scheduler(UserService userService, ExpenseService expenseService, IncomeService incomeService) {
        this.userService = userService;
        this.expenseService = expenseService;
        this.incomeService = incomeService;
    }

    @Scheduled(cron = "0 0 0 * * ?")  // Fire at midnight  // 0 * 11 * * ? // for testing purposes every minute after 11 AM
    public void reportForYesterday() {
        for(User user : userService.getAll()) {
            ReportThread reportThread = new ReportThread(expenseService, incomeService, user.getUsername());
            reportThread.start();
        }
    }
}

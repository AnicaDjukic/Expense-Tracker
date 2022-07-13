package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.model.User;
import com.expense.ExpenseTracker.service.AsyncService;
import com.expense.ExpenseTracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Scheduler {

    private final UserService userService;

    private final AsyncService reportThread;

    public Scheduler(UserService userService, AsyncService reportThread) {
        this.userService = userService;
        this.reportThread = reportThread;
    }

    @Scheduled(cron = "0 * 15 * * ?")   // 0 0 0 * * ? // Fire at midnight  // 0 * 11 * * ? // for testing purposes every minute after 11 AM
    public void reportForYesterday() {
        for (User user : userService.getAll()) {
            reportThread.run(user.getUsername());
        }
    }
}

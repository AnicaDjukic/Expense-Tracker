package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Scheduler {

    private final UserService userService;

    private final AsyncService asyncService;

    public Scheduler(UserService userService, AsyncService asyncService) {
        this.userService = userService;
        this.asyncService = asyncService;
    }

    @Scheduled(cron = "0 0 0 * * ?")    // Fire at midnight  // 0 * 16 * * ? // for testing purposes every minute after 16
    public void reportForYesterday() {
        for (User user : userService.getAll()) {
            asyncService.execute(user.getUsername());
        }
    }
}

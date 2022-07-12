package com.expense.ExpenseTracker.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.model.Income;
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


    @Scheduled(cron = "0 * 15 * * ?")  // 0 0 12 * * ? // Fire at 12:00 PM (noon) every day
    public void reportForYesterday() {
        for(User user : userService.getAll()) {
            log.info("USER : " + user.getUsername());
            log.info("Report for : " + LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
            log.info("Expense amount : " + calculateExpenseAmount(user.getUsername()));
            log.info("Income amount : " + calculateIncomeAmount(user.getUsername()));
            log.info("Total amount : " + (calculateIncomeAmount(user.getUsername()) - calculateExpenseAmount(user.getUsername())));
        }
    }

    private double calculateExpenseAmount(String username) {
        double amount = 0;
        List<Expense> expenses = expenseService.getExpensesForYesterday(username);
        for(Expense expense : expenses)
            amount += expense.getAmount();
        return amount;
    }

    private double calculateIncomeAmount(String username) {
        double amount = 0;
        List<Income> incomes = incomeService.getIncomesForYesterday(username);
        for(Income income : incomes)
            amount += income.getAmount();
        return amount;
    }
}

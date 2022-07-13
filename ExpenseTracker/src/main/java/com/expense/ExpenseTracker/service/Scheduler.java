package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.model.Income;
import com.expense.ExpenseTracker.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    @Scheduled(cron = "0 * 13 * * ?")  // 0 0 0 * * ? // Fire at midnight  // 0 * 11 * * ? // for testing purposes every minute after 11 AM
    public void reportForYesterday() {
        for(User user : userService.getAll()) {
            executeAsync(user.getUsername());
        }
    }

    @Async("asyncServiceExecutor")
    public void executeAsync(String username) {
        log.info(username + " " + LocalDateTime.now());
        createFile(username);
    }

    private void createFile(String username) {
        String fileName = username + LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".txt";
        File file = new File(fileName);
        try {
            if(file.createNewFile())
                writeToFile(username, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeToFile(String username, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName, true);
        StringBuilder str = new StringBuilder("USER : " + username).append("\n");
        str.append("Report for : ").append(LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
        str.append("Expense amount : ").append(calculateExpenseAmount(username)).append("\n");
        str.append("Income amount : ").append(calculateIncomeAmount(username)).append("\n");
        str.append("Total amount : ").append(calculateIncomeAmount(username) - calculateExpenseAmount(username)).append("\n");
        byte[] b= str.toString().getBytes();       //converts string into bytes
        fos.write(b);
        fos.close();
        log.info("Generated report for user : " + username);
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

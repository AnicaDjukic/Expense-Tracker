package com.expense.ExpenseTracker.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    @Scheduled(cron = "0 * 16 * * ?")  // 0 0 12 * * ? // Fire at 12:00 PM (noon) every day
    public void reportForYesterday() throws IOException {
        for(User user : userService.getAll()) {
            String fileName = user.getUsername() + LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".txt";
            File file = new File(fileName);
            if(file.createNewFile())
                writeToFile(user, fileName);
        }
    }

    private void writeToFile(User user, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName, true);
        StringBuilder str = new StringBuilder("USER : " + user.getUsername()).append("\n");
        str.append("Report for : ").append(LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
        str.append("Expense amount : ").append(calculateExpenseAmount(user.getUsername())).append("\n");
        str.append("Income amount : ").append(calculateIncomeAmount(user.getUsername())).append("\n");
        str.append("Total amount : ").append(calculateIncomeAmount(user.getUsername()) - calculateExpenseAmount(user.getUsername())).append("\n");
        byte[] b= str.toString().getBytes();       //converts string into bytes
        fos.write(b);
        fos.close();
        log.info("Generated report for user : " + user.getUsername());
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

package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.model.Income;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class AsyncService {

    private final ExpenseService expenseService;

    private final IncomeService incomeService;


    public AsyncService(ExpenseService expenseService, IncomeService incomeService) {
        this.expenseService = expenseService;
        this.incomeService = incomeService;
    }

    @Async("asyncServiceExecutor")
    public void execute(String username) {
        log.info(username + " " + LocalDateTime.now() + " thread id: " + Thread.currentThread().getId());
        String fileName = username + LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".txt";
        try {
            writeToFile(username, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void writeToFile(String username, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName, false);
        StringBuilder str = new StringBuilder("USER : " + username).append("\n");
        str.append("Report for : ");
        str.append(LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
        str.append("Expense amount : ").append(calculateExpenseAmount(username)).append("\n");
        str.append("Income amount : ").append(calculateIncomeAmount(username)).append("\n");
        str.append("Total amount : ");
        str.append(calculateIncomeAmount(username) - calculateExpenseAmount(username)).append("\n");
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


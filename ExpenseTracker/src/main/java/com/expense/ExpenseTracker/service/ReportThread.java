package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.model.Income;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class ReportThread implements Runnable{

    private final ExpenseService expenseService;

    private final IncomeService incomeService;

    private final String username;

    private Thread thread;

    public ReportThread(ExpenseService expenseService, IncomeService incomeService, String username) {
        this.expenseService = expenseService;
        this.incomeService = incomeService;
        this.username = username;
    }


    @Override
    public void run() {
        String fileName = username + LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".txt";
        File file = new File(fileName);
        try {
            if(file.createNewFile())
                writeToFile(username, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        if(thread == null) {
            thread = new Thread(this);
            log.info(username + " " + LocalDateTime.now() + " thread id: " + thread.getId());
            thread.start();
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

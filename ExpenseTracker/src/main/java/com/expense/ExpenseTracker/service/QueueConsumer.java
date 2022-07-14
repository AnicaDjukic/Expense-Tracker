package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.model.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class QueueConsumer {

    private final UserService userService;

    private final ExpenseGroupService expenseGroupService;

    private final ExpenseService expenseService;

    private final IncomeGroupService incomeGroupService;

    private final IncomeService incomeService;

    public QueueConsumer(UserService userService, ExpenseGroupService expenseGroupService, ExpenseService expenseService, IncomeGroupService incomeGroupService, IncomeService incomeService) {
        this.userService = userService;
        this.expenseGroupService = expenseGroupService;
        this.expenseService = expenseService;
        this.incomeGroupService = incomeGroupService;
        this.incomeService = incomeService;
    }

    @RabbitListener(queues = {"expense-groups"})
    public void receiveExpenseGroup(@Payload String fileBody) {
        System.out.println("Message " + fileBody);
        JsonObject convertedObject = new Gson().fromJson(fileBody, JsonObject.class);
        System.out.println(convertedObject.toString());

        ExpenseGroup expenseGroup = new ExpenseGroup(convertedObject.get("name").getAsString(), convertedObject.get("description").getAsString());
        User user = userService.getById(UUID.fromString(convertedObject.get("userId").getAsString()));
        ExpenseGroup savedExpenseGroup = expenseGroupService.addNew(expenseGroup, user.getUsername());
        System.out.println(savedExpenseGroup);
    }

    @RabbitListener(queues = {"expenses"})
    public void receiveExpense(@Payload String fileBody) {
        System.out.println("Message " + fileBody);
        JsonObject convertedObject = new Gson().fromJson(fileBody, JsonObject.class);
        System.out.println(convertedObject.toString());

        Expense expense = new Expense(convertedObject.get("description").getAsString(),
                convertedObject.get("amount").getAsDouble());
        User user = userService.getById(UUID.fromString(convertedObject.get("userId").getAsString()));
        Expense savedExpense = expenseService.addNew(expense, UUID.fromString(convertedObject.get("expenseGroupId").getAsString()), user.getUsername());
        System.out.println(savedExpense);
    }

    @RabbitListener(queues = {"income-groups"})
    public void receiveIncomeGroup(@Payload String fileBody) {
        System.out.println("Message " + fileBody);
        JsonObject convertedObject = new Gson().fromJson(fileBody, JsonObject.class);
        System.out.println(convertedObject.toString());

        IncomeGroup incomeGroup = new IncomeGroup(convertedObject.get("name").getAsString(), convertedObject.get("description").getAsString());
        User user = userService.getById(UUID.fromString(convertedObject.get("userId").getAsString()));
        IncomeGroup savedIncomeGroup = incomeGroupService.addNew(incomeGroup, user.getUsername());
        System.out.println(savedIncomeGroup);
    }

    @RabbitListener(queues = {"incomes"})
    public void receiveIncome(@Payload String fileBody) {
        System.out.println("Message " + fileBody);
        JsonObject convertedObject = new Gson().fromJson(fileBody, JsonObject.class);
        System.out.println(convertedObject.toString());

        Income income = new Income(convertedObject.get("description").getAsString(),
                convertedObject.get("amount").getAsDouble());
        User user = userService.getById(UUID.fromString(convertedObject.get("userId").getAsString()));
        Income savedIncome = incomeService.addNew(income, UUID.fromString(convertedObject.get("incomeGroupId").getAsString()), user.getUsername());
        System.out.println(savedIncome);
    }

}

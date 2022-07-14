package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.model.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@Slf4j
public class QueueConsumer {

    private final ExpenseGroupService expenseGroupService;

    private final ExpenseService expenseService;

    private final IncomeGroupService incomeGroupService;

    private final IncomeService incomeService;

    public QueueConsumer(ExpenseGroupService expenseGroupService, ExpenseService expenseService, IncomeGroupService incomeGroupService, IncomeService incomeService) {
        this.expenseGroupService = expenseGroupService;
        this.expenseService = expenseService;
        this.incomeGroupService = incomeGroupService;
        this.incomeService = incomeService;
    }

    @RabbitListener(queues = {"expense-groups"})
    public void receiveExpenseGroup(@Payload String fileBody) {
        try {
            JsonObject convertedObject = new Gson().fromJson(fileBody, JsonObject.class);
            log.info(convertedObject.toString());
            ExpenseGroup expenseGroup = new ExpenseGroup(convertedObject.get("name").getAsString(),
                    convertedObject.get("description").getAsString());
            ExpenseGroup savedExpenseGroup = expenseGroupService.addNewByMQ(expenseGroup,
                    convertedObject.get("userId").getAsString());
            if(savedExpenseGroup != null)  log.info("Successfully saved: " + savedExpenseGroup);
            else log.warn("Saving new expense group failed");
        } catch (JsonSyntaxException exception) {
            log.warn("Invalid json format!");
        }
    }

    @RabbitListener(queues = {"expenses"})
    public void receiveExpense(@Payload String fileBody) {
        try {
            JsonObject convertedObject = new Gson().fromJson(fileBody, JsonObject.class);
            log.info(convertedObject.toString());
            Expense expense = new Expense(convertedObject.get("description").getAsString(),
                    convertedObject.get("amount").getAsDouble());
            Expense savedExpense = expenseService.addNewByMQ(expense,
                    UUID.fromString(convertedObject.get("expenseGroupId").getAsString()),
                    convertedObject.get("userId").getAsString());
            if(savedExpense != null)  log.info("Successfully saved: " + savedExpense);
            else log.warn("Saving new expense failed");
        } catch (JsonSyntaxException exception) {
            log.warn("Invalid json format!");
        }

    }

    @RabbitListener(queues = {"income-groups"})
    public void receiveIncomeGroup(@Payload String fileBody) {
        try {
            JsonObject convertedObject = new Gson().fromJson(fileBody, JsonObject.class);
            log.info(convertedObject.toString());
            IncomeGroup incomeGroup = new IncomeGroup(convertedObject.get("name").getAsString(),
                    convertedObject.get("description").getAsString());
            IncomeGroup savedIncomeGroup = incomeGroupService.addNewByMQ(incomeGroup,
                    convertedObject.get("userId").getAsString());
            if(savedIncomeGroup != null)  log.info("Successfully saved: " + savedIncomeGroup);
            else log.warn("Saving new income group failed");
        } catch (JsonSyntaxException exception) {
            log.warn("Invalid json format!");
        }
    }

    @RabbitListener(queues = {"incomes"})
    public void receiveIncome(@Payload String fileBody) {
        try {
            JsonObject convertedObject = new Gson().fromJson(fileBody, JsonObject.class);
            log.info(convertedObject.toString());
            Income income = new Income(convertedObject.get("description").getAsString(),
                    convertedObject.get("amount").getAsDouble());
            Income savedIncome = incomeService.addNewByMq(income,
                    UUID.fromString(convertedObject.get("incomeGroupId").getAsString()),
                    convertedObject.get("userId").getAsString());
            if(savedIncome != null)  log.info("Successfully saved: " + savedIncome);
            else log.warn("Saving new income failed");
        } catch (JsonSyntaxException exception) {
            log.warn("Invalid json format!");
        }
    }

}

package com.expense.ExpenseTracker.message_queue;

import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.service.ExpenseService;
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
public class ExpenseMQConsumer {

    private final ExpenseService expenseService;

    public ExpenseMQConsumer(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @RabbitListener(queues = {"expenses"})
    public void receiveExpense(@Payload String fileBody) {
        try {
            JsonObject convertedObject = new Gson().fromJson(fileBody, JsonObject.class);
            log.info(convertedObject.toString());
            createNewExpense(convertedObject);
        } catch (JsonSyntaxException exception) {
            log.warn("Invalid json format!");
        }

    }

    private void createNewExpense(JsonObject convertedObject) {
        Expense expense = new Expense(convertedObject.get("description").getAsString(),
                convertedObject.get("amount").getAsDouble());
        Expense savedExpense = expenseService.addNewByMQ(expense,
                UUID.fromString(convertedObject.get("expenseGroupId").getAsString()),
                convertedObject.get("userId").getAsString());
        if(savedExpense != null)  log.info("Successfully saved: " + savedExpense);
        else log.warn("Saving new expense failed");
    }
}

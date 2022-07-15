package com.expense.ExpenseTracker.message_queue;

import com.expense.ExpenseTracker.model.ExpenseGroup;
import com.expense.ExpenseTracker.service.ExpenseGroupService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExpenseGroupMQConsumer {

    private final ExpenseGroupService expenseGroupService;

    public ExpenseGroupMQConsumer(ExpenseGroupService expenseGroupService) {
        this.expenseGroupService = expenseGroupService;
    }

    @RabbitListener(queues = {"expense-groups"})
    public void receiveExpenseGroup(@Payload String fileBody) {
        try {
            JsonObject convertedObject = new Gson().fromJson(fileBody, JsonObject.class);
            log.info(convertedObject.toString());
            createNewExpenseGroup(convertedObject);
        } catch (JsonSyntaxException exception) {
            log.warn("Invalid json format!");
        }
    }

    private void createNewExpenseGroup(JsonObject convertedObject) {
        ExpenseGroup expenseGroup = new ExpenseGroup(convertedObject.get("name").getAsString(),
                convertedObject.get("description").getAsString());
        ExpenseGroup savedExpenseGroup = expenseGroupService.addNewByMQ(expenseGroup,
                convertedObject.get("userId").getAsString());
        if(savedExpenseGroup != null)  log.info("Successfully saved: " + savedExpenseGroup);
        else log.warn("Saving new expense group failed");
    }
}

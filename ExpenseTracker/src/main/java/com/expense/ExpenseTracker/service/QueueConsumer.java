package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.model.ExpenseGroup;
import com.expense.ExpenseTracker.model.User;
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

    public QueueConsumer(UserService userService, ExpenseGroupService expenseGroupService) {
        this.userService = userService;
        this.expenseGroupService = expenseGroupService;
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

}

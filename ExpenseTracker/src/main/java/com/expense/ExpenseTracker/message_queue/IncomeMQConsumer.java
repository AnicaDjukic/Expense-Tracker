package com.expense.ExpenseTracker.message_queue;

import com.expense.ExpenseTracker.model.Income;
import com.expense.ExpenseTracker.service.IncomeService;
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
public class IncomeMQConsumer {

    private final IncomeService incomeService;

    private final Gson gson;

    public IncomeMQConsumer(IncomeService incomeService) {
        this.incomeService = incomeService;
        this.gson = new Gson();
    }

    @RabbitListener(queues = {"incomes"})
    public void receiveIncome(@Payload String fileBody) {
        try {
            JsonObject convertedObject = gson.fromJson(fileBody, JsonObject.class);
            log.info(convertedObject.toString());
            createNewIncome(convertedObject);
        } catch (JsonSyntaxException exception) {
            log.warn("Invalid json format!");
        }
    }

    private void createNewIncome(JsonObject convertedObject) {
        Income income = new Income(convertedObject.get("description").getAsString(),
                convertedObject.get("amount").getAsDouble());
        Income savedIncome = incomeService.addNewByMq(income,
                UUID.fromString(convertedObject.get("incomeGroupId").getAsString()),
                convertedObject.get("userId").getAsString());
        if(savedIncome != null) {
            log.info("Successfully saved: " + savedIncome);
        } else {
            log.warn("Saving new income failed");
        }
    }
}

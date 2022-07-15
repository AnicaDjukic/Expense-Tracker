package com.expense.ExpenseTracker.message_queue;

import com.expense.ExpenseTracker.model.IncomeGroup;
import com.expense.ExpenseTracker.service.IncomeGroupService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IncomeGroupMQConsumer {

    private final IncomeGroupService incomeGroupService;

    private final Gson gson;

    public IncomeGroupMQConsumer(IncomeGroupService incomeGroupService) {
        this.incomeGroupService = incomeGroupService;
        this.gson = new Gson();
    }

    @RabbitListener(queues = {"income-groups"})
    public void receiveIncomeGroup(@Payload String fileBody) {
        try {
            JsonObject convertedObject = gson.fromJson(fileBody, JsonObject.class);
            log.info(convertedObject.toString());
            createNewIncomeGroup(convertedObject);
        } catch (JsonSyntaxException exception) {
            log.warn("Invalid json format!");
        }
    }

    private void createNewIncomeGroup(JsonObject convertedObject) {
        IncomeGroup incomeGroup = new IncomeGroup(convertedObject.get("name").getAsString(),
                convertedObject.get("description").getAsString());
        IncomeGroup savedIncomeGroup = incomeGroupService.addNewByMQ(incomeGroup,
                convertedObject.get("userId").getAsString());
        if(savedIncomeGroup != null)  {
            log.info("Successfully saved: " + savedIncomeGroup);
        } else {
            log.warn("Saving new income group failed");
        }
    }
}

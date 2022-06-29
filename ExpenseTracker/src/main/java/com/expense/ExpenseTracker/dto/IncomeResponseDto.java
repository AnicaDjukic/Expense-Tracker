package com.expense.ExpenseTracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IncomeResponseDto {

    private UUID id;

    private String description;

    private double amount;

    private IncomeGroupViewDto incomeGroup;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date creationTime;
}

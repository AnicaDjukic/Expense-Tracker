package com.expense.ExpenseTracker.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Income {

    @Id
    @GeneratedValue
    private UUID id;

    @Setter
    private String description;
    @Setter
    private double amount;
    @Setter
    @ManyToOne
    @JoinColumn
    private IncomeGroup incomeGroup;

    public Income(String description, double amount, IncomeGroup incomeGroup) {
        this.description = description;
        this.amount = amount;
        this.incomeGroup = incomeGroup;
    }

    public Income(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }
}

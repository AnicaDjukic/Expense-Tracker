package com.expense.ExpenseTracker.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue
    private UUID id;

    @Setter
    private String description;

    @Setter
    private double amount;

    @ManyToOne
    @JoinColumn
    private ExpenseGroup expenseGroup;

    public Expense(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }
}

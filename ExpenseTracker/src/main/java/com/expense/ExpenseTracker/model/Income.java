package com.expense.ExpenseTracker.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Income {

    @Id
    private Long id;

    private String description;

    private int amount;

    @ManyToOne
    @JoinColumn
    private IncomeGroup incomeGroup;

    public Income() {

    }

    public Income(String description, int amount, IncomeGroup incomeGroup) {
        this.description = description;
        this.amount = amount;
        this.incomeGroup = incomeGroup;
    }


    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public IncomeGroup getIncomeGroup() {
        return incomeGroup;
    }
}

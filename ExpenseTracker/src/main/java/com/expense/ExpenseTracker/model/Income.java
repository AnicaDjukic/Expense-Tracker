package com.expense.ExpenseTracker.model;

import javax.persistence.*;

@Entity
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private double amount;

    @ManyToOne
    @JoinColumn
    private IncomeGroup incomeGroup;

    public Income() {

    }

    public Income(String description, double amount, IncomeGroup incomeGroup) {
        this.description = description;
        this.amount = amount;
        this.incomeGroup = incomeGroup;
    }

    public Income(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setIncomeGroup(IncomeGroup incomeGroup) {
        this.incomeGroup = incomeGroup;
    }

    public IncomeGroup getIncomeGroup() {
        return incomeGroup;
    }
}

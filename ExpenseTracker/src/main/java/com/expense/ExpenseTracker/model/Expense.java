package com.expense.ExpenseTracker.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Expense {

    @Id
    private Long id;

    private int amount;

    @ManyToOne
    @JoinColumn
    private ExpenseGroup expenseGroup;

    public Expense() {

    }

    public Expense(int amount, ExpenseGroup expenseGroup) {
        this.amount = amount;
        this.expenseGroup = expenseGroup;
    }


    public Long getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public ExpenseGroup getExpenseGroup() {
        return expenseGroup;
    }
}

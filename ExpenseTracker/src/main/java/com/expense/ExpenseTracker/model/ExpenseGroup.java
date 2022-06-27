package com.expense.ExpenseTracker.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ExpenseGroup {

    @Id
    private Long id;

    private String name;

    private String description;

    public ExpenseGroup() {

    }

    public ExpenseGroup(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

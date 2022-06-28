package com.expense.ExpenseTracker.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class ExpenseGroup {

    @Id
    @GeneratedValue
    private UUID id;

    @Setter
    private String name;

    @Setter
    private String description;

    public ExpenseGroup(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

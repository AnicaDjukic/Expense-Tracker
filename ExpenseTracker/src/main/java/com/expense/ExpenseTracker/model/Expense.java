package com.expense.ExpenseTracker.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Expense {

    @Id
    @GeneratedValue
    private UUID id;

    @Setter
    private String description;

    @Setter
    private double amount;

    @Setter
    private LocalDateTime creationTime;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @ToString.Exclude
    private ExpenseGroup expenseGroup;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @ToString.Exclude
    private User user;

    @PrePersist
    protected void onCreate() {
        creationTime = LocalDateTime.now();
    }

    public Expense(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }
}

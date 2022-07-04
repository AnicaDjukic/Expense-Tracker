package com.expense.ExpenseTracker.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Setter
    private LocalDateTime creationTime;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ExpenseGroup expenseGroup;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @PrePersist
    protected void onCreate() {
        creationTime = LocalDateTime.now();
    }
}

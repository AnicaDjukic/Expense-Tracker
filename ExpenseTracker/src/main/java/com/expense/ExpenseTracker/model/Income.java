package com.expense.ExpenseTracker.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Income {

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
    private IncomeGroup incomeGroup;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @ToString.Exclude
    private User user;

    @PrePersist
    protected void onCreate() {
        creationTime = LocalDateTime.now();
    }

    public Income(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

}

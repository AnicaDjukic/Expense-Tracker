package com.expense.ExpenseTracker.repository;

import com.expense.ExpenseTracker.model.QExpense;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

@Repository
public class QExpenseRepository {

    private final EntityManager entityManager;

    public QExpenseRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<QExpense> getLastFive() {
        QExpense expense = QExpense.expense;
        JPAQuery<QExpense> query = new JPAQuery<>(entityManager);
        query.from(expense).orderBy(expense.creationTime.desc()).limit(5);
        return query.fetch();
    }

    public List<QExpense> getLastFiveByExpenseGroupId(UUID expenseGroupId) {
        QExpense expense = QExpense.expense;
        JPAQuery<QExpense> query = new JPAQuery<>(entityManager);
        query.from(expense).where(expense.expenseGroup.id.eq(expenseGroupId)).orderBy(expense.creationTime.desc()).limit(5);
        return query.fetch();
    }
}

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

    public List<QExpense> getLastFew(int size, UUID userId) {
        QExpense expense = QExpense.expense;
        JPAQuery<QExpense> query = new JPAQuery<>(entityManager);
        query.from(expense).where(expense.user.id.eq(userId)).orderBy(expense.creationTime.desc()).limit(size);
        return query.fetch();
    }

    public List<QExpense> getLastFewByExpenseGroupId(UUID expenseGroupId, int size) {
        QExpense expense = QExpense.expense;
        JPAQuery<QExpense> query = new JPAQuery<>(entityManager);
        query.from(expense).where(expense.expenseGroup.id.eq(expenseGroupId)).orderBy(expense.creationTime.desc()).limit(size);
        return query.fetch();
    }
}

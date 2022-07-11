package com.expense.ExpenseTracker.repository;

import com.expense.ExpenseTracker.model.QIncome;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

@Repository
public class QIncomeRepository {
    private final EntityManager entityManager;

    public QIncomeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<QIncome> getLastFew(int size, String username) {
        QIncome income = QIncome.income;
        JPAQuery<QIncome> query = new JPAQuery<>(entityManager);
        query.from(income).where(income.user.username.eq(username)).orderBy(income.creationTime.desc()).limit(size);
        return query.fetch();
    }

    public List<QIncome> getLastFewByIncomeGroupId(UUID incomeGroupId, int size) {
        QIncome income = QIncome.income;
        JPAQuery<QIncome> query = new JPAQuery<>(entityManager);
        query.from(income).where(income.incomeGroup.id.eq(incomeGroupId)).orderBy(income.creationTime.desc()).limit(size);
        return query.fetch();
    }
}

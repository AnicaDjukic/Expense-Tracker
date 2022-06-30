package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.ExpenseRequestDto;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.model.QExpense;
import com.expense.ExpenseTracker.repository.ExpenseRepository;
import com.querydsl.jpa.impl.JPAQuery;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ExpenseService {

    private final ExpenseRepository repository;

    private final ExpenseGroupService expenseGroupService;

    private final EntityManager entityManager;

    private final ModelMapper modelMapper = new ModelMapper();

    public ExpenseService(ExpenseRepository repository, ExpenseGroupService expenseGroupService, EntityManager entityManager) {
        this.repository = repository;
        this.expenseGroupService = expenseGroupService;
        this.entityManager = entityManager;
    }

    public Expense addNew(Expense expense, UUID expenseGroupId) throws NotFoundException {
        expense.setCreationTime(new Date());
        expense.setExpenseGroup(expenseGroupService.getById(expenseGroupId));
        return repository.save(expense);
    }

    public List<Expense> getAll() {
        return repository.findAll();
    }

    public List<Expense> getLastFive() {
        QExpense expense = QExpense.expense;
        JPAQuery<QExpense> query = new JPAQuery<>(entityManager);
        query.from(expense).orderBy(expense.creationTime.desc()).limit(5);
        List<Expense> expenses = new ArrayList<>();
        for (int i = 0; i < query.fetch().size(); i++) {
            expenses.add(modelMapper.map(query.fetch().get(i), Expense.class));
        }
        return expenses;
    }

    public Expense getById(UUID id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(Expense.class.getSimpleName()));
    }

    public Expense update(UUID id, ExpenseRequestDto updateDto) throws NotFoundException {
        Expense expense = repository.findById(id).orElseThrow(() -> new NotFoundException(Expense.class.getSimpleName()));
        expense.setDescription(updateDto.getDescription());
        expense.setAmount(updateDto.getAmount());
        expense.setExpenseGroup(expenseGroupService.getById(updateDto.getExpenseGroupId()));
        return repository.save(expense);
    }

    public void deleteById(UUID id) throws NotFoundException {
        Expense expense = repository.findById(id).orElseThrow(() -> new NotFoundException(Expense.class.getSimpleName()));
        repository.delete(expense);
    }

    public List<Expense> getByExpenseGroupId(UUID expenseGroupId) {
        QExpense expense = QExpense.expense;
        JPAQuery<QExpense> query = new JPAQuery<>(entityManager);
        query.from(expense).where(expense.expenseGroup.id.eq(expenseGroupId)).orderBy(expense.creationTime.desc()).limit(5);
        List<Expense> expenses = new ArrayList<>();
        for (int i = 0; i < query.fetch().size(); i++) {
            expenses.add(modelMapper.map(query.fetch().get(i), Expense.class));
        }
        return expenses;
    }
}

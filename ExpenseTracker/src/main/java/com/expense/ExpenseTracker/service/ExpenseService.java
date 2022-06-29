package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.ExpenseRequestDto;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ExpenseService {

    private final ExpenseRepository repository;

    private final ExpenseGroupService expenseGroupService;

    public ExpenseService(ExpenseRepository repository, ExpenseGroupService expenseGroupService) {
        this.repository = repository;
        this.expenseGroupService = expenseGroupService;
    }

    public Expense addNew(Expense expense, UUID expenseGroupId) throws NotFoundException {
        expense.setCreationTime(new Date());
        expense.setExpenseGroup(expenseGroupService.getById(expenseGroupId));
        return repository.save(expense);
    }

    public List<Expense> getAll() {
        return repository.findAll();
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
        return repository.findByExpenseGroupOrderByCreationTimeDesc(expenseGroupService.getById(expenseGroupId));
    }
}

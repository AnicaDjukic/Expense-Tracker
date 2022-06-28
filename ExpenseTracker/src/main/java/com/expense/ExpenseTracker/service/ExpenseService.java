package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.ExpenseRequestDto;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    public Expense addNew(Expense expense) {
        return repository.save(expense);
    }

    public List<Expense> getAll() {
        return repository.findAll();
    }

    public Optional<Expense> getById(Long id) {
        return repository.findById(id);
    }

    public Expense update(Long id, ExpenseRequestDto updateDto) throws NotFoundException {
        Expense expense = getById(id).orElseThrow(NotFoundException::new);
        expense.setDescription(updateDto.getDescription());
        expense.setAmount(updateDto.getAmount());
        return repository.save(expense);
    }

    public void deleteById(Long id) throws NotFoundException {
        Expense expense = getById(id).orElseThrow(NotFoundException::new);
        repository.delete(expense);
    }
}

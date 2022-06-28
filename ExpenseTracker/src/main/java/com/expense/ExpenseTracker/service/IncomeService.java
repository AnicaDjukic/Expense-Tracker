package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.IncomeRequestDto;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.Income;
import com.expense.ExpenseTracker.repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IncomeService {

    private final IncomeRepository repository;

    public IncomeService(IncomeRepository repository) {
        this.repository = repository;
    }

    public Income addNew(Income income) {
        return repository.save(income);
    }

    public List<Income> getAll() {
        return repository.findAll();
    }

    public Income getById(UUID id) throws NotFoundException {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    public Income update(UUID id, IncomeRequestDto updateDto) throws NotFoundException {
        Income income = repository.findById(id).orElseThrow(NotFoundException::new);
        income.setDescription(updateDto.getDescription());
        income.setAmount(updateDto.getAmount());
        return repository.save(income);
    }

    public void deleteById(UUID id) throws NotFoundException {
        Income income = repository.findById(id).orElseThrow(NotFoundException::new);
        repository.delete(income);
    }
}

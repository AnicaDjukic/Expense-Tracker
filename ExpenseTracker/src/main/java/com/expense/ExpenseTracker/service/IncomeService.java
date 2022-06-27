package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.IncomeRequestDto;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.Income;
import com.expense.ExpenseTracker.repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Income> getById(Long id) {
        return repository.findById(id);
    }

    public Income update(Long id, IncomeRequestDto updateDto) throws NotFoundException {
        Income income = getById(id).orElseThrow(NotFoundException::new);
        income.setDescription(updateDto.getDescription());
        income.setAmount(updateDto.getAmount());
        return repository.save(income);
    }

    public void deleteById(Long id) throws NotFoundException {
        Income income = getById(id).orElseThrow(NotFoundException::new);
        repository.delete(income);
    }
}

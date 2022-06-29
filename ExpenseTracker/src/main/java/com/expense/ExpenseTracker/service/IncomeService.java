package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.IncomeRequestDto;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.Income;
import com.expense.ExpenseTracker.repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class IncomeService {

    private final IncomeRepository repository;

    private final IncomeGroupService incomeGroupService;

    public IncomeService(IncomeRepository repository, IncomeGroupService incomeGroupService) {
        this.repository = repository;
        this.incomeGroupService = incomeGroupService;
    }

    public Income addNew(Income income, UUID incomeGroupId) throws NotFoundException {
        income.setCreationTime(new Date());
        income.setIncomeGroup(incomeGroupService.getById(incomeGroupId));
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
        income.setIncomeGroup(incomeGroupService.getById(updateDto.getIncomeGroupId()));
        return repository.save(income);
    }

    public void deleteById(UUID id) throws NotFoundException {
        Income income = repository.findById(id).orElseThrow(NotFoundException::new);
        repository.delete(income);
    }
}

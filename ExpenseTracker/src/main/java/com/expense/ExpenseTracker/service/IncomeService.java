package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.IncomeRequestDto;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.Income;
import com.expense.ExpenseTracker.model.QIncome;
import com.expense.ExpenseTracker.repository.QIncomeRepository;
import com.expense.ExpenseTracker.repository.IncomeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class IncomeService {

    private final IncomeRepository repository;

    private final IncomeGroupService incomeGroupService;

    private final QIncomeRepository customRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public IncomeService(IncomeRepository repository, IncomeGroupService incomeGroupService, QIncomeRepository customRepository) {
        this.repository = repository;
        this.incomeGroupService = incomeGroupService;
        this.customRepository = customRepository;
    }

    public Income addNew(Income income, UUID incomeGroupId) throws NotFoundException {
        income.setIncomeGroup(incomeGroupService.getById(incomeGroupId));
        return repository.save(income);
    }

    public Page<Income> getAll(int pageNo, int size) {
        return repository.findAll(PageRequest.of(pageNo, size, Sort.by("creationTime").descending()));
    }

    public List<Income> getAll() {
        return repository.findAll();
    }

    public List<Income> getLastFive() {
        List<QIncome> qExpenses = customRepository.getLastFive();
        List<Income> incomes = new ArrayList<>();
        for (int i = 0; i < qExpenses.size(); i++) {
            incomes.add(modelMapper.map(qExpenses.get(i), Income.class));
        }
        return incomes;
    }

    public Income getById(UUID id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(Income.class.getSimpleName()));
    }

    public Income update(UUID id, IncomeRequestDto updateDto) throws NotFoundException {
        Income income = repository.findById(id).orElseThrow(() -> new NotFoundException(Income.class.getSimpleName()));
        income.setDescription(updateDto.getDescription());
        income.setAmount(updateDto.getAmount());
        income.setIncomeGroup(incomeGroupService.getById(updateDto.getIncomeGroupId()));
        return repository.save(income);
    }

    public void deleteById(UUID id) throws NotFoundException {
        Income income = repository.findById(id).orElseThrow(() -> new NotFoundException(Income.class.getSimpleName()));
        repository.delete(income);
    }

    public List<Income> getByIncomeGroupId(UUID incomeGroupId) {
        List<QIncome> qIncomes = customRepository.getLastFiveByIncomeGroupId(incomeGroupId);
        List<Income> incomes = new ArrayList<>();
        for (int i = 0; i < qIncomes.size(); i++) {
            incomes.add(modelMapper.map(qIncomes.get(i), Income.class));
        }
        return incomes;
    }
}

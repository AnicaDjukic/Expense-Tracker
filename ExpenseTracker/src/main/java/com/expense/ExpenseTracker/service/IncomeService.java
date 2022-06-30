package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.IncomeRequestDto;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.Income;
import com.expense.ExpenseTracker.model.QIncome;
import com.expense.ExpenseTracker.repository.IncomeRepository;
import com.querydsl.jpa.impl.JPAQuery;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class IncomeService {

    private final IncomeRepository repository;

    private final IncomeGroupService incomeGroupService;

    private final EntityManager entityManager;

    private final ModelMapper modelMapper = new ModelMapper();

    public IncomeService(IncomeRepository repository, IncomeGroupService incomeGroupService, EntityManager entityManager) {
        this.repository = repository;
        this.incomeGroupService = incomeGroupService;
        this.entityManager = entityManager;
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
        QIncome income = QIncome.income;
        JPAQuery<QIncome> query = new JPAQuery<>(entityManager);
        query.from(income).orderBy(income.creationTime.desc()).limit(5);
        List<Income> incomes = new ArrayList<>();
        for (int i = 0; i < query.fetch().size(); i++) {
            incomes.add(modelMapper.map(query.fetch().get(i), Income.class));
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
        QIncome income = QIncome.income;
        JPAQuery<QIncome> query = new JPAQuery<>(entityManager);
        query.from(income).where(income.incomeGroup.id.eq(incomeGroupId)).orderBy(income.creationTime.desc()).limit(5);
        List<Income> incomes = new ArrayList<>();
        for (int i = 0; i < query.fetch().size(); i++) {
            incomes.add(modelMapper.map(query.fetch().get(i), Income.class));
        }
        return incomes;
    }
}

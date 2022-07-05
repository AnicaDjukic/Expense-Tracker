package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.IncomeRequestDto;
import com.expense.ExpenseTracker.exception.AccessResourceDeniedException;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.Expense;
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

    private final QIncomeRepository qRepository;

    private final IncomeGroupService incomeGroupService;

    private final UserService userService;

    private final ModelMapper modelMapper = new ModelMapper();

    public IncomeService(IncomeRepository repository, QIncomeRepository qRepository, IncomeGroupService incomeGroupService, UserService userService) {
        this.repository = repository;
        this.qRepository = qRepository;
        this.incomeGroupService = incomeGroupService;
        this.userService = userService;
    }

    public Income addNew(Income income, UUID incomeGroupId, UUID userId) throws NotFoundException {
        income.setIncomeGroup(incomeGroupService.getById(incomeGroupId, userId));
        income.setUser(userService.getById(userId));
        return repository.save(income);
    }

    public Page<Income> getAll(int pageNo, int size, UUID userId) {
        return repository.findByUser(userService.getById(userId), PageRequest.of(pageNo, size, Sort.by("creationTime").descending()));
    }

    public List<Income> getAll() {
        return repository.findAll();
    }

    public List<Income> getLastFew(int size) {
        List<QIncome> qExpenses = qRepository.getLastFew(size);
        List<Income> incomes = new ArrayList<>();
        for (int i = 0; i < qExpenses.size(); i++) {
            incomes.add(modelMapper.map(qExpenses.get(i), Income.class));
        }
        return incomes;
    }

    public Income update(UUID id, IncomeRequestDto updateDto, UUID userId) throws NotFoundException {
        Income income = getByIdAndUserId(id, userId);
        income.setDescription(updateDto.getDescription());
        income.setAmount(updateDto.getAmount());
        income.setIncomeGroup(incomeGroupService.getById(updateDto.getIncomeGroupId(), userId));
        return repository.save(income);
    }

    public void deleteById(UUID id, UUID userId) throws NotFoundException {
        Income income = getByIdAndUserId(id, userId);
        repository.delete(income);
    }

    public List<Income> getByIncomeGroupId(UUID incomeGroupId, int size, UUID userId) {
        incomeGroupService.getById(incomeGroupId, userId);
        List<QIncome> qIncomes = qRepository.getLastFewByIncomeGroupId(incomeGroupId, size);
        List<Income> incomes = new ArrayList<>();
        for (int i = 0; i < qIncomes.size(); i++) {
            incomes.add(modelMapper.map(qIncomes.get(i), Income.class));
        }
        return incomes;
    }

    public Income getByIdAndUserId(UUID id, UUID userId) {
        repository.findById(id).orElseThrow(() -> new NotFoundException(Income.class.getSimpleName()));
        return repository.findByIdAndUser(id, userService.getById(userId)).orElseThrow(() -> new AccessResourceDeniedException(Expense.class.getSimpleName()));
    }
}

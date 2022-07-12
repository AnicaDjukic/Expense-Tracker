package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.IncomeRequestDto;
import com.expense.ExpenseTracker.exception.AccessResourceDeniedException;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.model.Income;
import com.expense.ExpenseTracker.model.QIncome;
import com.expense.ExpenseTracker.model.User;
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

    public Income addNew(Income income, UUID incomeGroupId, String username) throws NotFoundException {
        income.setIncomeGroup(incomeGroupService.getByIdAndUserUsername(incomeGroupId, username));
        income.setUser(userService.getByUsername(username));
        return repository.save(income);
    }

    public Page<Income> getAll(int pageNo, int size, String username) {
        User user = userService.getByUsername(username);
        return repository.findByUser(user, PageRequest.of(pageNo, size, Sort.by("creationTime").descending()));
    }

    public List<Income> getAll(String username) {
        User user = userService.getByUsername(username);
        return repository.findByUser(user);
    }

    public List<Income> getLastFew(int size, String username) {
        List<QIncome> qExpenses = qRepository.getLastFew(size, username);
        List<Income> incomes = new ArrayList<>();
        for (int i = 0; i < qExpenses.size(); i++) {
            incomes.add(modelMapper.map(qExpenses.get(i), Income.class));
        }
        return incomes;
    }

    public Income update(UUID id, IncomeRequestDto updateDto, String username) throws NotFoundException {
        Income income = getByIdAndUserUsername(id, username);
        income.setDescription(updateDto.getDescription());
        income.setAmount(updateDto.getAmount());
        income.setIncomeGroup(incomeGroupService.getByIdAndUserUsername(updateDto.getIncomeGroupId(), username));
        return repository.save(income);
    }

    public void deleteById(UUID id, String username) throws NotFoundException {
        Income income = getByIdAndUserUsername(id, username);
        repository.delete(income);
    }

    public List<Income> getByIncomeGroupId(UUID incomeGroupId, int size, String username) {
        incomeGroupService.getByIdAndUserUsername(incomeGroupId, username);
        List<QIncome> qIncomes = qRepository.getLastFewByIncomeGroupId(incomeGroupId, size);
        List<Income> incomes = new ArrayList<>();
        for (int i = 0; i < qIncomes.size(); i++) {
            incomes.add(modelMapper.map(qIncomes.get(i), Income.class));
        }
        return incomes;
    }

    public Income getByIdAndUserUsername(UUID id, String username) {
        User user = userService.getByUsername(username);
        repository.findById(id).orElseThrow(() -> new NotFoundException(Income.class.getSimpleName()));
        return repository.findByIdAndUser(id, user).orElseThrow(() -> new AccessResourceDeniedException(Income.class.getSimpleName()));
    }

    public List<Income> getIncomesForYesterday(String username) {
        List<QIncome> qIncomes = qRepository.getIncomesForYesterday(username);
        List<Income> incomes = new ArrayList<>();
        for (int i = 0; i < qIncomes.size(); i++) {
            incomes.add(modelMapper.map(qIncomes.get(i), Income.class));
        }
        return incomes;
    }
}

package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.ExpenseRequestDto;
import com.expense.ExpenseTracker.exception.AccessResourceDeniedException;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.model.QExpense;
import com.expense.ExpenseTracker.model.User;
import com.expense.ExpenseTracker.repository.QExpenseRepository;
import com.expense.ExpenseTracker.repository.ExpenseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExpenseService {

    private final ExpenseRepository repository;

    private final QExpenseRepository qRepository;

    private final ExpenseGroupService expenseGroupService;

    private final UserService userService;

    private final ModelMapper modelMapper = new ModelMapper();

    public ExpenseService(ExpenseRepository repository, QExpenseRepository qRepository, ExpenseGroupService expenseGroupService, UserService userService) {
        this.repository = repository;
        this.qRepository = qRepository;
        this.expenseGroupService = expenseGroupService;
        this.userService = userService;
    }

    public Expense addNew(Expense expense, UUID expenseGroupId, UUID userId) throws NotFoundException {
        expense.setExpenseGroup(expenseGroupService.getByIdAndUserId(expenseGroupId, userId));
        expense.setUser(userService.getById(userId));
        return repository.save(expense);
    }

    public Page<Expense> getAll(int pageNo, int size, UUID userId) {
        User user = userService.getById(userId);
        return repository.findByUser(user, PageRequest.of(pageNo, size, Sort.by("creationTime").descending()));
    }

    public List<Expense> getAll() {
        return repository.findAll();
    }

    public List<Expense> getLastFew(int size) {
        List<QExpense> qExpenses = qRepository.getLastFew(size);
        List<Expense> expenses = new ArrayList<>();
        for (int i = 0; i < qExpenses.size(); i++) {
            expenses.add(modelMapper.map(qExpenses.get(i), Expense.class));
        }
        return expenses;
    }

    public Expense getByIdAndUserId(UUID id, UUID userId) throws NotFoundException {
        User user = userService.getById(userId);
        repository.findById(id).orElseThrow(() -> new NotFoundException(Expense.class.getSimpleName()));
        return repository.findByIdAndUser(id, user).orElseThrow(() -> new AccessResourceDeniedException(Expense.class.getSimpleName()));
    }

    public Expense update(UUID id, ExpenseRequestDto updateDto, UUID userId) throws NotFoundException {
        Expense expense = getByIdAndUserId(id, userId);
        expense.setDescription(updateDto.getDescription());
        expense.setAmount(updateDto.getAmount());
        expense.setExpenseGroup(expenseGroupService.getByIdAndUserId(updateDto.getExpenseGroupId(), userId));
        return repository.save(expense);
    }

    public void deleteById(UUID id, UUID userId) throws NotFoundException {
        Expense expense = getByIdAndUserId(id, userId);
        repository.delete(expense);
    }

    public List<Expense> getByExpenseGroupId(UUID expenseGroupId, int size, UUID userId) {
        expenseGroupService.getByIdAndUserId(expenseGroupId, userId);
        List<QExpense> qExpenses = qRepository.getLastFewByExpenseGroupId(expenseGroupId, size);
        List<Expense> expenses = new ArrayList<>();
        for (int i = 0; i < qExpenses.size(); i++) {
            expenses.add(modelMapper.map(qExpenses.get(i), Expense.class));
        }
        return expenses;
    }
}

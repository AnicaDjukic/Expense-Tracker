package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.ExpenseRequestDto;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.model.QExpense;
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

    private final ExpenseGroupService expenseGroupService;

    private final QExpenseRepository qRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public ExpenseService(ExpenseRepository repository, ExpenseGroupService expenseGroupService, QExpenseRepository qRepository) {
        this.repository = repository;
        this.expenseGroupService = expenseGroupService;
        this.qRepository = qRepository;
    }

    public Expense addNew(Expense expense, UUID expenseGroupId) throws NotFoundException {
        expense.setExpenseGroup(expenseGroupService.getById(expenseGroupId));
        return repository.save(expense);
    }

    public Page<Expense> getAll(int pageNo, int size) {
        return repository.findAll(PageRequest.of(pageNo, size, Sort.by("creationTime").descending()));
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

    public List<Expense> getByExpenseGroupId(UUID expenseGroupId, int size) {
        List<QExpense> qExpenses = qRepository.getLastFewByExpenseGroupId(expenseGroupId, size);
        List<Expense> expenses = new ArrayList<>();
        for (int i = 0; i < qExpenses.size(); i++) {
            expenses.add(modelMapper.map(qExpenses.get(i), Expense.class));
        }
        return expenses;
    }
}

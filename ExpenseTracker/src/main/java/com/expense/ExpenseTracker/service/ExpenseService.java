package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.ExpenseRequestDto;
import com.expense.ExpenseTracker.exception.AccessResourceDeniedException;
import com.expense.ExpenseTracker.exception.NameAlreadyExistsException;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.model.QExpense;
import com.expense.ExpenseTracker.model.User;
import com.expense.ExpenseTracker.repository.QExpenseRepository;
import com.expense.ExpenseTracker.repository.ExpenseRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ExpenseService {

    private final ExpenseRepository repository;

    private final QExpenseRepository qRepository;

    private final ExpenseGroupService expenseGroupService;

    private final UserService userService;

    private final QueueSender queueSender;

    private final ModelMapper modelMapper = new ModelMapper();

    public ExpenseService(ExpenseRepository repository, QExpenseRepository qRepository, ExpenseGroupService expenseGroupService, UserService userService, QueueSender queueSender) {
        this.repository = repository;
        this.qRepository = qRepository;
        this.expenseGroupService = expenseGroupService;
        this.userService = userService;
        this.queueSender = queueSender;
    }

    public Expense addNew(Expense expense, UUID expenseGroupId, String username) throws NotFoundException {
        expense.setExpenseGroup(expenseGroupService.getByIdAndUserUsername(expenseGroupId, username));
        expense.setUser(userService.getByUsername(username));
        return repository.save(expense);
    }

    public Page<Expense> getAll(int pageNo, int size, String username) {
        User user = userService.getByUsername(username);
        return repository.findByUser(user, PageRequest.of(pageNo, size, Sort.by("creationTime").descending()));
    }

    public List<Expense> getAll(String username) {
        User user = userService.getByUsername(username);
        return repository.findByUser(user);
    }

    public List<Expense> getLastFew(int size, String username) {
        List<QExpense> qExpenses = qRepository.getLastFew(size, username);
        List<Expense> expenses = new ArrayList<>();
        for (int i = 0; i < qExpenses.size(); i++) {
            expenses.add(modelMapper.map(qExpenses.get(i), Expense.class));
        }
        return expenses;
    }

    public Expense update(UUID id, ExpenseRequestDto updateDto, String username) throws NotFoundException {
        Expense expense = getByIdAndUsername(id, username);
        expense.setDescription(updateDto.getDescription());
        expense.setAmount(updateDto.getAmount());
        expense.setExpenseGroup(expenseGroupService.getByIdAndUserUsername(updateDto.getExpenseGroupId(), username));
        return repository.save(expense);
    }

    public void deleteById(UUID id, String username) throws NotFoundException {
        Expense expense = getByIdAndUsername(id, username);
        repository.delete(expense);
    }

    public List<Expense> getByExpenseGroupId(UUID expenseGroupId, int size, String username) {
        expenseGroupService.getByIdAndUserUsername(expenseGroupId, username);
        List<QExpense> qExpenses = qRepository.getLastFewByExpenseGroupId(expenseGroupId, size);
        List<Expense> expenses = new ArrayList<>();
        for (int i = 0; i < qExpenses.size(); i++) {
            expenses.add(modelMapper.map(qExpenses.get(i), Expense.class));
        }
        return expenses;
    }

    public Expense getByIdAndUsername(UUID id, String username) {
        User user = userService.getByUsername(username);
        repository.findById(id).orElseThrow(() -> new NotFoundException(Expense.class.getSimpleName()));
        return repository.findByIdAndUser(id, user).orElseThrow(() -> new AccessResourceDeniedException(Expense.class.getSimpleName()));
    }

    public List<Expense> getExpensesForYesterday(String username) {
        List<QExpense> qExpenses = qRepository.getExpensesForYesterday(username);
        List<Expense> expenses = new ArrayList<>();
        for (int i = 0; i < qExpenses.size(); i++) {
            expenses.add(modelMapper.map(qExpenses.get(i), Expense.class));
        }
        return expenses;
    }

    public Expense addNewByMQ(Expense expense, UUID expenseGroupId, String userId) {
        try {
            User user = userService.getById(UUID.fromString(userId));
            return addNew(expense, expenseGroupId, user.getUsername());
        } catch (NotFoundException | AccessResourceDeniedException ex) {
            queueSender.send(ex.getMessage() + " dateTime: " + LocalDateTime.now());
            log.info(ex.getMessage() + " dateTime: " + LocalDateTime.now());
            return null;
        }
    }
}

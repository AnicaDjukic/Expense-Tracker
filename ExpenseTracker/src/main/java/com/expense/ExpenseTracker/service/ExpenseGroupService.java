package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.ExpenseGroupRequestDto;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.ExpenseGroup;
import com.expense.ExpenseTracker.repository.ExpenseGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExpenseGroupService {
    private final ExpenseGroupRepository repository;

    public ExpenseGroupService(ExpenseGroupRepository repository) {
        this.repository = repository;
    }

    public ExpenseGroup addNew(ExpenseGroup expenseGroup) {
        return repository.save(expenseGroup);
    }

    public List<ExpenseGroup> getAll() {
        return repository.findAll();
    }

    public ExpenseGroup getById(UUID id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(ExpenseGroup.class.getSimpleName()));
    }

    public ExpenseGroup update(UUID id, ExpenseGroupRequestDto updateDto) throws NotFoundException {
        ExpenseGroup expenseGroup = repository.findById(id).orElseThrow(() -> new NotFoundException(ExpenseGroup.class.getSimpleName()));
        expenseGroup.setName(updateDto.getName());
        expenseGroup.setDescription(updateDto.getDescription());
        return repository.save(expenseGroup);
    }

    public void deleteById(UUID id) throws NotFoundException {
        ExpenseGroup expenseGroup = repository.findById(id).orElseThrow(() -> new NotFoundException(ExpenseGroup.class.getSimpleName()));
        repository.delete(expenseGroup);
    }
}

package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.ExpenseGroupRequestDto;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.ExpenseGroup;
import com.expense.ExpenseTracker.repository.ExpenseGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<ExpenseGroup> getById(Long id) {
        return repository.findById(id);
    }

    public ExpenseGroup update(Long id, ExpenseGroupRequestDto updateDto) throws NotFoundException {
        ExpenseGroup expenseGroup = getById(id).orElseThrow(NotFoundException::new);
        expenseGroup.setName(updateDto.getName());
        expenseGroup.setDescription(updateDto.getDescription());
        return repository.save(expenseGroup);
    }

    public void deleteById(Long id) throws NotFoundException {
        ExpenseGroup expenseGroup = getById(id).orElseThrow(NotFoundException::new);
        repository.delete(expenseGroup);
    }
}

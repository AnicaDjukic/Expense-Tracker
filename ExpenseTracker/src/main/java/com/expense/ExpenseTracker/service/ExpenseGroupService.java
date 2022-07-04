package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.ExpenseGroupRequestDto;
import com.expense.ExpenseTracker.exception.NameAlreadyExistsException;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.ExpenseGroup;
import com.expense.ExpenseTracker.repository.ExpenseGroupRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ExpenseGroupService {
    private final ExpenseGroupRepository repository;

    private final UserService userService;

    public ExpenseGroupService(ExpenseGroupRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public ExpenseGroup addNew(ExpenseGroup expenseGroup, UUID userId) {
        repository.findByNameAndUser(expenseGroup.getName(), userService.getById(userId))
                .ifPresent(existingExpenseGroup -> {throw new NameAlreadyExistsException(ExpenseGroup.class.getSimpleName(), expenseGroup.getName());});
        expenseGroup.setUser(userService.getById(userId));
        return repository.save(expenseGroup);
    }

    public Page<ExpenseGroup> getAll(int pageNo, int size, UUID userId) {
        return repository.findByUser(userService.getById(userId), PageRequest.of(pageNo, size));
    }

    public ExpenseGroup getById(UUID id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(ExpenseGroup.class.getSimpleName()));
    }

    public ExpenseGroup update(UUID id, ExpenseGroupRequestDto updateDto) throws NotFoundException {
        Optional<ExpenseGroup> existingExpGroup = repository.findByName(updateDto.getName());
        if (existingExpGroup.isPresent() && !existingExpGroup.get().getId().equals(id))
            throw new NameAlreadyExistsException(ExpenseGroup.class.getSimpleName(), updateDto.getName());
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

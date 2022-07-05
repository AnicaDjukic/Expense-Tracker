package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.ExpenseGroupRequestDto;
import com.expense.ExpenseTracker.exception.AccessResourceDeniedException;
import com.expense.ExpenseTracker.exception.NameAlreadyExistsException;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.ExpenseGroup;
import com.expense.ExpenseTracker.model.User;
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
        User user = userService.getById(userId);
        repository.findByNameAndUser(expenseGroup.getName(), user)
                .ifPresent(expGroup -> {throw new NameAlreadyExistsException(ExpenseGroup.class.getSimpleName(), expenseGroup.getName());});
        expenseGroup.setUser(user);
        return repository.save(expenseGroup);
    }

    public Page<ExpenseGroup> getAll(int pageNo, int size, UUID userId) {
        User user = userService.getById(userId);
        return repository.findByUser(user, PageRequest.of(pageNo, size));
    }

    public ExpenseGroup update(UUID id, ExpenseGroupRequestDto updateDto, UUID userId) throws NotFoundException {
        Optional<ExpenseGroup> existingExpGroup = findByNameAndUserId(updateDto.getName(), userId);
        if (existingExpGroup.isPresent() && !existingExpGroup.get().getId().equals(id))
            throw new NameAlreadyExistsException(ExpenseGroup.class.getSimpleName(), updateDto.getName());
        ExpenseGroup expenseGroup = getByIdAndUserId(id, userId);
        expenseGroup.setName(updateDto.getName());
        expenseGroup.setDescription(updateDto.getDescription());
        return repository.save(expenseGroup);
    }

    public void deleteById(UUID id, UUID userId) throws NotFoundException {
        ExpenseGroup expenseGroup = getByIdAndUserId(id, userId);
        repository.delete(expenseGroup);
    }

    private Optional<ExpenseGroup> findByNameAndUserId(String name, UUID userId) {
        User user = userService.getById(userId);
        return repository.findByNameAndUser(name, user);
    }

    public ExpenseGroup getByIdAndUserId(UUID id, UUID userId) {
        User user = userService.getById(userId);
        repository.findById(id).orElseThrow(() -> new NotFoundException(ExpenseGroup.class.getSimpleName()));
        return repository.findByIdAndUser(id, user).orElseThrow(() -> new AccessResourceDeniedException(ExpenseGroup.class.getSimpleName()));
    }
}

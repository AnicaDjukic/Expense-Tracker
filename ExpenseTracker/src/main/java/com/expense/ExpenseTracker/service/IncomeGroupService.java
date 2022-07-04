package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.ExpenseGroupRequestDto;
import com.expense.ExpenseTracker.exception.NameAlreadyExistsException;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.IncomeGroup;
import com.expense.ExpenseTracker.model.User;
import com.expense.ExpenseTracker.repository.IncomeGroupRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class IncomeGroupService {

    private final IncomeGroupRepository repository;

    private final UserService userService;

    public IncomeGroupService(IncomeGroupRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public IncomeGroup addNew(IncomeGroup incomeGroup, UUID userId) {
        repository.findByNameAndUser(incomeGroup.getName(), userService.getById(userId))
                .ifPresent(existingIncomeGroup -> {throw new NameAlreadyExistsException(IncomeGroup.class.getSimpleName(), incomeGroup.getName());});
        incomeGroup.setUser(userService.getById(userId));
        return repository.save(incomeGroup);
    }

    public Page<IncomeGroup> getAll(int pageNo, int size, UUID userId) {
        return repository.findByUser(userService.getById(userId), PageRequest.of(pageNo, size));
    }

    public IncomeGroup getById(UUID id, UUID userId) throws NotFoundException {
        return getByIdAndUser(id, userService.getById(userId));
    }

    public IncomeGroup update(UUID id, ExpenseGroupRequestDto updateDto, UUID userId) throws NotFoundException {
        Optional<IncomeGroup> existingIncGroup = repository.findByNameAndUser(updateDto.getName(), userService.getById(userId));
        if (existingIncGroup.isPresent() && !existingIncGroup.get().getId().equals(id))
            throw new NameAlreadyExistsException(IncomeGroup.class.getSimpleName(), updateDto.getName());
        IncomeGroup incomeGroup = getByIdAndUser(id, userService.getById(userId));
        incomeGroup.setName(updateDto.getName());
        incomeGroup.setDescription(updateDto.getDescription());
        return repository.save(incomeGroup);
    }

    public void deleteById(UUID id, UUID userId) throws NotFoundException {
        IncomeGroup incomeGroup = getByIdAndUser(id, userService.getById(userId));
        repository.delete(incomeGroup);
    }

    public IncomeGroup getByIdAndUser(UUID id, User user) {
        repository.findById(id).orElseThrow(() -> new NotFoundException(IncomeGroup.class.getSimpleName()));
        return repository.findByIdAndUser(id, user).orElseThrow(() -> new AccessDeniedException(IncomeGroup.class.getSimpleName()));
    }
}

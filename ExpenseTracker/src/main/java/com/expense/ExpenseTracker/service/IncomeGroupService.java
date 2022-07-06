package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.ExpenseGroupRequestDto;
import com.expense.ExpenseTracker.exception.AccessResourceDeniedException;
import com.expense.ExpenseTracker.exception.NameAlreadyExistsException;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.IncomeGroup;
import com.expense.ExpenseTracker.model.User;
import com.expense.ExpenseTracker.repository.IncomeGroupRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        User user = userService.getById(userId);
        repository.findByNameAndUser(incomeGroup.getName(), user)
                .ifPresent(incGroup -> {throw new NameAlreadyExistsException(IncomeGroup.class.getSimpleName(), incomeGroup.getName());});
        incomeGroup.setUser(userService.getById(userId));
        return repository.save(incomeGroup);
    }

    public Page<IncomeGroup> getAll(int pageNo, int size, UUID userId) {
        User user = userService.getById(userId);
        return repository.findByUser(user, PageRequest.of(pageNo, size));
    }

    public IncomeGroup update(UUID id, ExpenseGroupRequestDto updateDto, UUID userId) throws NotFoundException {
        User user = userService.getById(userId);
        Optional<IncomeGroup> existingIncGroup = repository.findByNameAndUser(updateDto.getName(), user);
        if (existingIncGroup.isPresent() && !existingIncGroup.get().getId().equals(id))
            throw new NameAlreadyExistsException(IncomeGroup.class.getSimpleName(), updateDto.getName());
        IncomeGroup incomeGroup = getByIdAndUserId(id, userId);
        incomeGroup.setName(updateDto.getName());
        incomeGroup.setDescription(updateDto.getDescription());
        return repository.save(incomeGroup);
    }

    public void deleteById(UUID id, UUID userId) throws NotFoundException {
        IncomeGroup incomeGroup = getByIdAndUserId(id, userId);
        repository.delete(incomeGroup);
    }

    public IncomeGroup getByIdAndUserId(UUID id, UUID userId) {
        User user = userService.getById(userId);
        repository.findById(id).orElseThrow(() -> new NotFoundException(IncomeGroup.class.getSimpleName()));
        return repository.findByIdAndUser(id, user).orElseThrow(() -> new AccessResourceDeniedException(IncomeGroup.class.getSimpleName()));
    }
}

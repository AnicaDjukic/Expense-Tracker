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

    public IncomeGroup addNew(IncomeGroup incomeGroup, String username) {
        User user = userService.getByUsername(username);
        repository.findByNameAndUser(incomeGroup.getName(), user)
                .ifPresent(incGroup -> {throw new NameAlreadyExistsException(IncomeGroup.class.getSimpleName(), incomeGroup.getName());});
        incomeGroup.setUser(userService.getByUsername(username));
        return repository.save(incomeGroup);
    }

    public Page<IncomeGroup> getAll(int pageNo, int size, String username) {
        User user = userService.getByUsername(username);
        return repository.findByUser(user, PageRequest.of(pageNo, size));
    }

    public IncomeGroup update(UUID id, ExpenseGroupRequestDto updateDto, String username) throws NotFoundException {
        User user = userService.getByUsername(username);
        Optional<IncomeGroup> existingIncGroup = repository.findByNameAndUser(updateDto.getName(), user);
        if (existingIncGroup.isPresent() && !existingIncGroup.get().getId().equals(id))
            throw new NameAlreadyExistsException(IncomeGroup.class.getSimpleName(), updateDto.getName());
        IncomeGroup incomeGroup = getByIdAndUserUsername(id, username);
        incomeGroup.setName(updateDto.getName());
        incomeGroup.setDescription(updateDto.getDescription());
        return repository.save(incomeGroup);
    }

    public void deleteById(UUID id, String username) throws NotFoundException {
        IncomeGroup incomeGroup = getByIdAndUserUsername(id, username);
        repository.delete(incomeGroup);
    }

    public IncomeGroup getByIdAndUserUsername(UUID id, String username) {
        User user = userService.getByUsername(username);
        repository.findById(id).orElseThrow(() -> new NotFoundException(IncomeGroup.class.getSimpleName()));
        return repository.findByIdAndUser(id, user).orElseThrow(() -> new AccessResourceDeniedException(IncomeGroup.class.getSimpleName()));
    }
}

package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.ExpenseGroupRequestDto;
import com.expense.ExpenseTracker.exception.NameAlreadyExistsException;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.IncomeGroup;
import com.expense.ExpenseTracker.repository.IncomeGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class IncomeGroupService {

    private final IncomeGroupRepository repository;

    public IncomeGroupService(IncomeGroupRepository repository) {
        this.repository = repository;
    }

    public IncomeGroup addNew(IncomeGroup incomeGroup) {
        repository.findByName(incomeGroup.getName())
                .ifPresent(existingIncomeGroup -> {throw new NameAlreadyExistsException(IncomeGroup.class.getSimpleName(), incomeGroup.getName());});
        return repository.save(incomeGroup);
    }

    public List<IncomeGroup> getAll() {
        return repository.findAll();
    }

    public IncomeGroup getById(UUID id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(IncomeGroup.class.getSimpleName()));
    }

    public IncomeGroup update(UUID id, ExpenseGroupRequestDto updateDto) throws NotFoundException {
        Optional<IncomeGroup> existingIncGroup = repository.findByName(updateDto.getName());
        if (existingIncGroup.isPresent() && !existingIncGroup.get().getId().equals(id))
            throw new NameAlreadyExistsException(IncomeGroup.class.getSimpleName(), updateDto.getName());
        IncomeGroup incomeGroup = repository.findById(id).orElseThrow(() -> new NotFoundException(IncomeGroup.class.getSimpleName()));
        incomeGroup.setName(updateDto.getName());
        incomeGroup.setDescription(updateDto.getDescription());
        return repository.save(incomeGroup);
    }

    public void deleteById(UUID id) throws NotFoundException {
        IncomeGroup incomeGroup = repository.findById(id).orElseThrow(() -> new NotFoundException(IncomeGroup.class.getSimpleName()));
        repository.delete(incomeGroup);
    }
}

package com.expense.ExpenseTracker.service;

import com.expense.ExpenseTracker.dto.ExpenseGroupRequestDto;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.IncomeGroup;
import com.expense.ExpenseTracker.repository.IncomeGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeGroupService {

    private final IncomeGroupRepository repository;

    public IncomeGroupService(IncomeGroupRepository repository) {
        this.repository = repository;
    }

    public IncomeGroup addNew(IncomeGroup incomeGroup) {
        return repository.save(incomeGroup);
    }

    public List<IncomeGroup> getAll() {
        return repository.findAll();
    }

    public IncomeGroup getById(Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    public IncomeGroup update(Long id, ExpenseGroupRequestDto updateDto) throws NotFoundException {
        IncomeGroup incomeGroup = repository.findById(id).orElseThrow(NotFoundException::new);
        incomeGroup.setName(updateDto.getName());
        incomeGroup.setDescription(updateDto.getDescription());
        return repository.save(incomeGroup);
    }

    public void deleteById(Long id) throws NotFoundException {
        IncomeGroup incomeGroup = repository.findById(id).orElseThrow(NotFoundException::new);
        repository.delete(incomeGroup);
    }
}

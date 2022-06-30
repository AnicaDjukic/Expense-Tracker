package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.ExpenseGroupRequestDto;
import com.expense.ExpenseTracker.dto.IncomeGroupRequestDto;
import com.expense.ExpenseTracker.dto.IncomeGroupResponseDto;
import com.expense.ExpenseTracker.model.IncomeGroup;
import com.expense.ExpenseTracker.service.IncomeGroupService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/income-groups")
public class IncomeGroupController {

    private final IncomeGroupService incomeGroupService;

    private final ModelMapper modelMapper = new ModelMapper();

    public IncomeGroupController(IncomeGroupService incomeGroupService) {
        this.incomeGroupService = incomeGroupService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public IncomeGroupResponseDto create(@RequestBody @Valid IncomeGroupRequestDto newExpenseGroupDto) {
        IncomeGroup expenseGroup = modelMapper.map(newExpenseGroupDto, IncomeGroup.class);
        IncomeGroup savedIncomeGroup = incomeGroupService.addNew(expenseGroup);
        return modelMapper.map(savedIncomeGroup, IncomeGroupResponseDto.class);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<IncomeGroupResponseDto> getAll() {
        List<IncomeGroup> incomeGroups = incomeGroupService.getAll();
        List<IncomeGroupResponseDto> incomeGroupDtos = new ArrayList<>();
        for(IncomeGroup expenseGroup : incomeGroups) {
            incomeGroupDtos.add(modelMapper.map(expenseGroup, IncomeGroupResponseDto.class));
        }
        return incomeGroupDtos;
    }

    @GetMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public IncomeGroupResponseDto getById(@PathVariable UUID id) {
        IncomeGroup incomeGroup = incomeGroupService.getById(id);
        return modelMapper.map(incomeGroup, IncomeGroupResponseDto.class);
    }

    @PutMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public IncomeGroupResponseDto update(@PathVariable UUID id, @RequestBody @Valid ExpenseGroupRequestDto updateDto) {
        IncomeGroup updatedIncomeGroup = incomeGroupService.update(id, updateDto);
        return modelMapper.map(updatedIncomeGroup, IncomeGroupResponseDto.class);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        incomeGroupService.deleteById(id);
    }

}

package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.ExpenseGroupRequestDto;
import com.expense.ExpenseTracker.dto.ExpenseGroupResponseDto;
import com.expense.ExpenseTracker.model.ExpenseGroup;
import com.expense.ExpenseTracker.service.ExpenseGroupService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/expense-groups")
public class ExpenseGroupController {

    private final ExpenseGroupService expenseGroupService;

    private final ModelMapper modelMapper = new ModelMapper();

    public ExpenseGroupController(ExpenseGroupService expenseGroupService) {
        this.expenseGroupService = expenseGroupService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ExpenseGroupResponseDto create(@RequestBody @Valid ExpenseGroupRequestDto newExpenseGroupDto) {
        ExpenseGroup expenseGroup = modelMapper.map(newExpenseGroupDto, ExpenseGroup.class);
        ExpenseGroup savedExpenseGroup = expenseGroupService.addNew(expenseGroup);
        return modelMapper.map(savedExpenseGroup, ExpenseGroupResponseDto.class);
    }

    @GetMapping("/{pageNo}/{size}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ExpenseGroupResponseDto> getAll(@PathVariable int pageNo, @PathVariable int size) {
        Page<ExpenseGroup> expenseGroups = expenseGroupService.getAll(pageNo, size);
        List<ExpenseGroupResponseDto> expenseGroupDtos = new ArrayList<>();
        for(ExpenseGroup expenseGroup : expenseGroups) {
            expenseGroupDtos.add(modelMapper.map(expenseGroup, ExpenseGroupResponseDto.class));
        }
        return expenseGroupDtos;
    }

    @GetMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ExpenseGroupResponseDto getById(@PathVariable UUID id) {
        ExpenseGroup expenseGroup = expenseGroupService.getById(id);
        return modelMapper.map(expenseGroup, ExpenseGroupResponseDto.class);
    }

    @PutMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ExpenseGroupResponseDto update(@PathVariable UUID id, @RequestBody @Valid ExpenseGroupRequestDto updateDto) {
        ExpenseGroup updatedExpenseGroup = expenseGroupService.update(id, updateDto);
        return modelMapper.map(updatedExpenseGroup, ExpenseGroupResponseDto.class);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        expenseGroupService.deleteById(id);
    }

}

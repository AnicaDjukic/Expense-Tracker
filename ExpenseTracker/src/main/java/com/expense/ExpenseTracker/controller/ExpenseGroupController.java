package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.ExpenseGroupRequestDto;
import com.expense.ExpenseTracker.dto.ExpenseGroupResponseDto;
import com.expense.ExpenseTracker.model.ExpenseGroup;
import com.expense.ExpenseTracker.model.User;
import com.expense.ExpenseTracker.service.ExpenseGroupService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/expense-groups")
public class ExpenseGroupController {

    private final ExpenseGroupService expenseGroupService;

    private final ModelMapper modelMapper = new ModelMapper();

    public ExpenseGroupController(ExpenseGroupService expenseGroupService) {
        this.expenseGroupService = expenseGroupService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ExpenseGroupResponseDto create(@RequestBody @Valid ExpenseGroupRequestDto newExpenseGroupDto, @AuthenticationPrincipal User authDto) {
        ExpenseGroup expenseGroup = modelMapper.map(newExpenseGroupDto, ExpenseGroup.class);
        ExpenseGroup savedExpenseGroup = expenseGroupService.addNew(expenseGroup, authDto.getId());
        return modelMapper.map(savedExpenseGroup, ExpenseGroupResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{pageNo}/{size}")
    @ResponseStatus(value = HttpStatus.OK)
    public Page<ExpenseGroupResponseDto> getAll(@PathVariable int pageNo, @PathVariable int size, @AuthenticationPrincipal User authDto) {
        Page<ExpenseGroup> expenseGroups = expenseGroupService.getAll(pageNo, size, authDto.getId());
        return expenseGroups.map(expenseGroup -> modelMapper.map(expenseGroup, ExpenseGroupResponseDto.class));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ExpenseGroupResponseDto getById(@PathVariable UUID id, @AuthenticationPrincipal User authDto) {
        ExpenseGroup expenseGroup = expenseGroupService.getById(id, authDto.getId());
        return modelMapper.map(expenseGroup, ExpenseGroupResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ExpenseGroupResponseDto update(@PathVariable UUID id, @RequestBody @Valid ExpenseGroupRequestDto updateDto, @AuthenticationPrincipal User authDto) {
        ExpenseGroup updatedExpenseGroup = expenseGroupService.update(id, updateDto, authDto.getId());
        return modelMapper.map(updatedExpenseGroup, ExpenseGroupResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable UUID id, @AuthenticationPrincipal User authDto) {
        expenseGroupService.deleteById(id, authDto.getId());
    }

}

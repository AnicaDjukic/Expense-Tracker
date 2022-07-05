package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.ExpenseGroupRequestDto;
import com.expense.ExpenseTracker.dto.IncomeGroupRequestDto;
import com.expense.ExpenseTracker.dto.IncomeGroupResponseDto;
import com.expense.ExpenseTracker.model.IncomeGroup;
import com.expense.ExpenseTracker.model.User;
import com.expense.ExpenseTracker.service.IncomeGroupService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/income-groups")
public class IncomeGroupController {

    private final IncomeGroupService incomeGroupService;

    private final ModelMapper modelMapper = new ModelMapper();

    public IncomeGroupController(IncomeGroupService incomeGroupService) {
        this.incomeGroupService = incomeGroupService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public IncomeGroupResponseDto create(@RequestBody @Valid IncomeGroupRequestDto newExpenseGroupDto, @AuthenticationPrincipal User authDto) {
        IncomeGroup expenseGroup = modelMapper.map(newExpenseGroupDto, IncomeGroup.class);
        IncomeGroup savedIncomeGroup = incomeGroupService.addNew(expenseGroup, authDto.getId());
        return modelMapper.map(savedIncomeGroup, IncomeGroupResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Page<IncomeGroupResponseDto> getAll(@RequestParam int page, @RequestParam int size, @AuthenticationPrincipal User authDto) {
        Page<IncomeGroup> incomeGroups = incomeGroupService.getAll(page, size, authDto.getId());
        return incomeGroups.map(incomeGroup -> modelMapper.map(incomeGroup, IncomeGroupResponseDto.class));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public IncomeGroupResponseDto getById(@PathVariable UUID id, @AuthenticationPrincipal User authDto) {
        IncomeGroup incomeGroup = incomeGroupService.getByIdAndUserId(id, authDto.getId());
        return modelMapper.map(incomeGroup, IncomeGroupResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public IncomeGroupResponseDto update(@PathVariable UUID id, @RequestBody @Valid ExpenseGroupRequestDto updateDto, @AuthenticationPrincipal User authDto) {
        IncomeGroup updatedIncomeGroup = incomeGroupService.update(id, updateDto, authDto.getId());
        return modelMapper.map(updatedIncomeGroup, IncomeGroupResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable UUID id, @AuthenticationPrincipal User authDto) {
        incomeGroupService.deleteById(id, authDto.getId());
    }

}

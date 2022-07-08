package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.ExpenseGroupRequestDto;
import com.expense.ExpenseTracker.dto.IncomeGroupRequestDto;
import com.expense.ExpenseTracker.dto.IncomeGroupResponseDto;
import com.expense.ExpenseTracker.model.IncomeGroup;
import com.expense.ExpenseTracker.security.SecurityUtil;
import com.expense.ExpenseTracker.service.IncomeGroupService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public IncomeGroupResponseDto create(@RequestBody @Valid IncomeGroupRequestDto newExpenseGroupDto) {
        String username = SecurityUtil.getLoggedIn().getName();
        IncomeGroup expenseGroup = modelMapper.map(newExpenseGroupDto, IncomeGroup.class);
        IncomeGroup savedIncomeGroup = incomeGroupService.addNew(expenseGroup, username);
        return modelMapper.map(savedIncomeGroup, IncomeGroupResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Page<IncomeGroupResponseDto> getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                               @RequestParam(required = false, defaultValue = "5") int size) {
        String username = SecurityUtil.getLoggedIn().getName();
        Page<IncomeGroup> incomeGroups = incomeGroupService.getAll(page, size, username);
        return incomeGroups.map(incomeGroup -> modelMapper.map(incomeGroup, IncomeGroupResponseDto.class));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public IncomeGroupResponseDto getById(@PathVariable UUID id) {
        String username = SecurityUtil.getLoggedIn().getName();
        IncomeGroup incomeGroup = incomeGroupService.getByIdAndUserUsername(id, username);
        return modelMapper.map(incomeGroup, IncomeGroupResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public IncomeGroupResponseDto update(@PathVariable UUID id,
                                         @RequestBody @Valid ExpenseGroupRequestDto updateDto) {
        String username = SecurityUtil.getLoggedIn().getName();
        IncomeGroup updatedIncomeGroup = incomeGroupService.update(id, updateDto, username);
        return modelMapper.map(updatedIncomeGroup, IncomeGroupResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        String username = SecurityUtil.getLoggedIn().getName();
        incomeGroupService.deleteById(id, username);
    }

}

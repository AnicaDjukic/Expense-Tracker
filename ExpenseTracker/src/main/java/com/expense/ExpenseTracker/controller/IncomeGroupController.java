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
        UUID userId = SecurityUtil.getLoggedUser().getId();
        IncomeGroup expenseGroup = modelMapper.map(newExpenseGroupDto, IncomeGroup.class);
        IncomeGroup savedIncomeGroup = incomeGroupService.addNew(expenseGroup, userId);
        return modelMapper.map(savedIncomeGroup, IncomeGroupResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Page<IncomeGroupResponseDto> getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                               @RequestParam(required = false, defaultValue = "5") int size) {
        UUID userId = SecurityUtil.getLoggedUser().getId();
        Page<IncomeGroup> incomeGroups = incomeGroupService.getAll(page, size, userId);
        return incomeGroups.map(incomeGroup -> modelMapper.map(incomeGroup, IncomeGroupResponseDto.class));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public IncomeGroupResponseDto getById(@PathVariable UUID id) {
        UUID userId = SecurityUtil.getLoggedUser().getId();
        IncomeGroup incomeGroup = incomeGroupService.getByIdAndUserId(id, userId);
        return modelMapper.map(incomeGroup, IncomeGroupResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public IncomeGroupResponseDto update(@PathVariable UUID id,
                                         @RequestBody @Valid ExpenseGroupRequestDto updateDto) {
        UUID userId = SecurityUtil.getLoggedUser().getId();
        IncomeGroup updatedIncomeGroup = incomeGroupService.update(id, updateDto, userId);
        return modelMapper.map(updatedIncomeGroup, IncomeGroupResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        UUID userId = SecurityUtil.getLoggedUser().getId();
        incomeGroupService.deleteById(id, userId);
    }

}

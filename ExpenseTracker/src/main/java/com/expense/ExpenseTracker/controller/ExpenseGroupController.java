package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.ExpenseGroupRequestDto;
import com.expense.ExpenseTracker.dto.ExpenseGroupResponseDto;
import com.expense.ExpenseTracker.model.ExpenseGroup;
import com.expense.ExpenseTracker.security.SecurityUtil;
import com.expense.ExpenseTracker.service.ExpenseGroupService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ExpenseGroupResponseDto create(@RequestBody @Valid ExpenseGroupRequestDto newExpenseGroupDto) {
        String username = SecurityUtil.getLoggedIn().getName();
        ExpenseGroup expenseGroup = modelMapper.map(newExpenseGroupDto, ExpenseGroup.class);
        ExpenseGroup savedExpenseGroup = expenseGroupService.addNew(expenseGroup, username);
        return modelMapper.map(savedExpenseGroup, ExpenseGroupResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Page<ExpenseGroupResponseDto> getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                                @RequestParam(required = false, defaultValue = "5") int size) {
        String username = SecurityUtil.getLoggedIn().getName();
        Page<ExpenseGroup> expenseGroups = expenseGroupService.getAll(page, size, username);
        return expenseGroups.map(expenseGroup -> modelMapper.map(expenseGroup, ExpenseGroupResponseDto.class));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ExpenseGroupResponseDto getById(@PathVariable UUID id) {
        String username = SecurityUtil.getLoggedIn().getName();
        ExpenseGroup expenseGroup = expenseGroupService.getByIdAndUserUsername(id, username);
        return modelMapper.map(expenseGroup, ExpenseGroupResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ExpenseGroupResponseDto update(@PathVariable UUID id,
                                          @RequestBody @Valid ExpenseGroupRequestDto updateDto) {
        String username = SecurityUtil.getLoggedIn().getName();
        ExpenseGroup updatedExpenseGroup = expenseGroupService.update(id, updateDto, username);
        return modelMapper.map(updatedExpenseGroup, ExpenseGroupResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        String username = SecurityUtil.getLoggedIn().getName();
        expenseGroupService.deleteById(id, username);
    }

}

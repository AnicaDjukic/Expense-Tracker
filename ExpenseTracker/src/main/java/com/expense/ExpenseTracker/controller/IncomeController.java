package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.IncomeRequestDto;
import com.expense.ExpenseTracker.dto.IncomeResponseDto;
import com.expense.ExpenseTracker.model.Income;
import com.expense.ExpenseTracker.security.SecurityUtil;
import com.expense.ExpenseTracker.service.IncomeService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1")
public class IncomeController {

    private final IncomeService incomeService;

    private final ModelMapper modelMapper = new ModelMapper();

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("incomes")
    @ResponseStatus(value = HttpStatus.CREATED)
    public IncomeResponseDto create(@RequestBody @Valid IncomeRequestDto newDto) {
        UUID userId = SecurityUtil.getLoggedUser().getId();
        Income income = modelMapper.map(newDto, Income.class);
        Income savedIncome = incomeService.addNew(income, newDto.getIncomeGroupId(), userId);
        return modelMapper.map(savedIncome, IncomeResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("incomes")
    @ResponseStatus(value = HttpStatus.OK)
    public Page<IncomeResponseDto> getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                          @RequestParam(required = false, defaultValue = "5") int size) {
        UUID userId = SecurityUtil.getLoggedUser().getId();
        Page<Income> incomes = incomeService.getAll(page, size, userId);
        return incomes.map(income -> modelMapper.map(income, IncomeResponseDto.class));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("income-groups/{id}/incomes")
    @ResponseStatus(value = HttpStatus.OK)
    public List<IncomeResponseDto> getLastFewForIncomeGroup(@PathVariable UUID id,
                                                            @RequestParam(required = false, defaultValue = "5") int size) {
        UUID userId = SecurityUtil.getLoggedUser().getId();
        List<Income> incomes = incomeService.getByIncomeGroupId(id, size, userId);
        return incomes.stream().map(income -> modelMapper.map(income, IncomeResponseDto.class)).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("incomes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public IncomeResponseDto getById(@PathVariable UUID id) {
        UUID userId = SecurityUtil.getLoggedUser().getId();
        Income income = incomeService.getByIdAndUserId(id, userId);
        return modelMapper.map(income, IncomeResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("incomes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public IncomeResponseDto update(@PathVariable UUID id,
                                    @RequestBody @Valid IncomeRequestDto updateDto) {
        UUID userId = SecurityUtil.getLoggedUser().getId();
        Income updatedIncome = incomeService.update(id, updateDto, userId);
        return modelMapper.map(updatedIncome, IncomeResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("incomes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        UUID userId = SecurityUtil.getLoggedUser().getId();
        incomeService.deleteById(id, userId);
    }
}

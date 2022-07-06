package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.IncomeRequestDto;
import com.expense.ExpenseTracker.dto.IncomeResponseDto;
import com.expense.ExpenseTracker.model.Income;
import com.expense.ExpenseTracker.model.User;
import com.expense.ExpenseTracker.service.IncomeService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public IncomeResponseDto create(@RequestBody @Valid IncomeRequestDto newDto,
                                    @AuthenticationPrincipal User authDto) {
        Income income = modelMapper.map(newDto, Income.class);
        Income savedIncome = incomeService.addNew(income, newDto.getIncomeGroupId(), authDto.getId());
        return modelMapper.map(savedIncome, IncomeResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("incomes")
    @ResponseStatus(value = HttpStatus.OK)
    public Page<IncomeResponseDto> getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                          @RequestParam(required = false, defaultValue = "5") int size,
                                          @AuthenticationPrincipal User authDto) {
        Page<Income> incomes = incomeService.getAll(page, size, authDto.getId());
        return incomes.map(income -> modelMapper.map(income, IncomeResponseDto.class));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("income-groups/{id}/incomes")
    @ResponseStatus(value = HttpStatus.OK)
    public List<IncomeResponseDto> getLastFewForIncomeGroup(@PathVariable UUID id,
                                                            @RequestParam(required = false, defaultValue = "5") int size,
                                                            @AuthenticationPrincipal User authDto) {
        List<Income> incomes = incomeService.getByIncomeGroupId(id, size, authDto.getId());
        return incomes.stream().map(income -> modelMapper.map(income, IncomeResponseDto.class)).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("incomes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public IncomeResponseDto getById(@PathVariable UUID id,
                                     @AuthenticationPrincipal User authDto) {
        Income income = incomeService.getByIdAndUserId(id, authDto.getId());
        return modelMapper.map(income, IncomeResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("incomes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public IncomeResponseDto update(@PathVariable UUID id,
                                    @RequestBody @Valid IncomeRequestDto updateDto,
                                    @AuthenticationPrincipal User authDto) {
        Income updatedIncome = incomeService.update(id, updateDto, authDto.getId());
        return modelMapper.map(updatedIncome, IncomeResponseDto.class);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("incomes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable UUID id, @AuthenticationPrincipal User authDto) {
        incomeService.deleteById(id, authDto.getId());
    }
}

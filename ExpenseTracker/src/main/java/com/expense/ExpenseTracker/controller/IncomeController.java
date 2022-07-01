package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.IncomeRequestDto;
import com.expense.ExpenseTracker.dto.IncomeResponseDto;
import com.expense.ExpenseTracker.model.Income;
import com.expense.ExpenseTracker.service.IncomeService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class IncomeController {

    private final IncomeService incomeService;

    private final ModelMapper modelMapper = new ModelMapper();

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping("incomes")
    @ResponseStatus(value = HttpStatus.CREATED)
    public IncomeResponseDto create(@RequestBody @Valid IncomeRequestDto newDto) {
        Income income = modelMapper.map(newDto, Income.class);
        Income savedIncome = incomeService.addNew(income, newDto.getIncomeGroupId());
        return modelMapper.map(savedIncome, IncomeResponseDto.class);
    }

    @GetMapping("incomes/{pageNo}/{size}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<IncomeResponseDto> getAll(@PathVariable int pageNo, @PathVariable int size) {
        Page<Income> incomes = incomeService.getAll(pageNo, size);
        List<IncomeResponseDto> incomeDtos = new ArrayList<>();
        for(Income income : incomes) {
            incomeDtos.add(modelMapper.map(income, IncomeResponseDto.class));
        }
        return incomeDtos;
    }

    @GetMapping("income-groups/{id}/incomes/{size}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<IncomeResponseDto> getLastFewForIncomeGroup(@PathVariable UUID id, @PathVariable int size) {
        List<Income> incomes = incomeService.getByIncomeGroupId(id, size);
        List<IncomeResponseDto> incomeDtos = new ArrayList<>();
        for(Income income : incomes) {
            incomeDtos.add(modelMapper.map(income, IncomeResponseDto.class));
        }
        return incomeDtos;
    }

    @GetMapping("incomes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public IncomeResponseDto getById(@PathVariable UUID id) {
        Income income = incomeService.getById(id);
        return modelMapper.map(income, IncomeResponseDto.class);
    }

    @PutMapping("incomes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public IncomeResponseDto update(@PathVariable UUID id, @RequestBody @Valid IncomeRequestDto updateDto) {
        Income updatedIncome = incomeService.update(id, updateDto);
        return modelMapper.map(updatedIncome, IncomeResponseDto.class);
    }

    @DeleteMapping("incomes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        incomeService.deleteById(id);
    }

}

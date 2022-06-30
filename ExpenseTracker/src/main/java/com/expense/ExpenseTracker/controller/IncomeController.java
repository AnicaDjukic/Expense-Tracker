package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.IncomeRequestDto;
import com.expense.ExpenseTracker.dto.IncomeResponseDto;
import com.expense.ExpenseTracker.model.Income;
import com.expense.ExpenseTracker.service.IncomeService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<IncomeResponseDto>> getAll(@PathVariable int pageNo, @PathVariable int size) {
        Page<Income> incomes = incomeService.getAll(pageNo, size);
        List<IncomeResponseDto> incomeDtos = new ArrayList<>();
        for(Income income : incomes) {
            incomeDtos.add(modelMapper.map(income, IncomeResponseDto.class));
        }
        return ResponseEntity.ok(incomeDtos);
    }

    @GetMapping("income-groups/{id}/incomes")
    public ResponseEntity<List<IncomeResponseDto>> getLastFiveForIncomeGroup(@PathVariable UUID id) {
        List<Income> incomes = incomeService.getByIncomeGroupId(id);
        List<IncomeResponseDto> incomeDtos = new ArrayList<>();
        for(Income income : incomes) {
            incomeDtos.add(modelMapper.map(income, IncomeResponseDto.class));
        }
        return ResponseEntity.ok(incomeDtos);
    }

    @GetMapping("incomes/{id}")
    public ResponseEntity<IncomeResponseDto> getById(@PathVariable UUID id) {
        Income income = incomeService.getById(id);
        return ResponseEntity.ok(modelMapper.map(income, IncomeResponseDto.class));
    }

    @PutMapping("incomes/{id}")
    public ResponseEntity<IncomeResponseDto> update(@PathVariable UUID id, @RequestBody @Valid IncomeRequestDto updateDto) {
        Income updatedIncome = incomeService.update(id, updateDto);
        return ResponseEntity.ok(modelMapper.map(updatedIncome, IncomeResponseDto.class));
    }

    @DeleteMapping("incomes/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@PathVariable UUID id) {
        incomeService.deleteById(id);
    }

}

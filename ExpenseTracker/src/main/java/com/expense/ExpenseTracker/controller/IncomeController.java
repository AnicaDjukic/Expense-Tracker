package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.IncomeRequestDto;
import com.expense.ExpenseTracker.dto.ExpenseResponseDto;
import com.expense.ExpenseTracker.dto.IncomeResponseDto;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.Income;
import com.expense.ExpenseTracker.service.IncomeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    public ResponseEntity<IncomeRequestDto> create(@RequestBody @Valid IncomeRequestDto newDto) {
        Income income = new Income(newDto.getDescription(), newDto.getAmount());
        Income savedIncome = incomeService.addNew(income);
        return new ResponseEntity(new ExpenseResponseDto(savedIncome.getId(), savedIncome.getDescription(), savedIncome.getAmount()), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<IncomeResponseDto>> getAll() {
        List<Income> incomes = incomeService.getAll();
        List<IncomeResponseDto> incomeDtos = new ArrayList<>();
        for(Income income : incomes) {
            incomeDtos.add(new IncomeResponseDto(income.getId(), income.getDescription(), income.getAmount()));
        }
        return ResponseEntity.ok(incomeDtos);
    }

    @GetMapping("{id}")
    public ResponseEntity<IncomeResponseDto> getById(@PathVariable Long id) throws NotFoundException {
        Income income = incomeService.getById(id);
        return ResponseEntity.ok(new IncomeResponseDto(income.getId(), income.getDescription(), income.getAmount()));
    }

    @PutMapping("{id}")
    public ResponseEntity<IncomeResponseDto> update(@PathVariable Long id, @RequestBody @Valid IncomeRequestDto updateDto) throws NotFoundException {
        Income updatedIncome = incomeService.update(id, updateDto);
        return ResponseEntity.ok(new IncomeResponseDto(updatedIncome.getId(), updatedIncome.getDescription(), updatedIncome.getAmount()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Long id) throws NotFoundException {
        incomeService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

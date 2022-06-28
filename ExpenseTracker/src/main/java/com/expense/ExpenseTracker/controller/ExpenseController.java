package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.ExpenseRequestDto;
import com.expense.ExpenseTracker.dto.ExpenseResponseDto;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.service.ExpenseService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ModelMapper modelMapper = new ModelMapper();

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDto> create(@RequestBody @Valid ExpenseRequestDto newExpenseDto) {
        Expense expense = modelMapper.map(newExpenseDto, Expense.class);
        Expense savedExpense = expenseService.addNew(expense);
        return new ResponseEntity(modelMapper.map(savedExpense, ExpenseResponseDto.class), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDto>> getAll() {
        List<Expense> expenses = expenseService.getAll();
        List<ExpenseResponseDto> expenseDtos = new ArrayList<>();
        for(Expense expense : expenses) {
            expenseDtos.add(modelMapper.map(expense, ExpenseResponseDto.class));
        }
        return ResponseEntity.ok(expenseDtos);
    }

    @GetMapping("{id}")
    public ResponseEntity<ExpenseResponseDto> getById(@PathVariable UUID id) throws NotFoundException {
        Expense expense = expenseService.getById(id);
        return ResponseEntity.ok(modelMapper.map(expense, ExpenseResponseDto.class));
    }

    @PutMapping("{id}")
    public ResponseEntity<ExpenseResponseDto> update(@PathVariable UUID id, @RequestBody @Valid ExpenseRequestDto updateDto) throws NotFoundException {
        Expense updatedExpense = expenseService.update(id, updateDto);
        return ResponseEntity.ok(modelMapper.map(updatedExpense, ExpenseResponseDto.class));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable UUID id) throws NotFoundException {
        expenseService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}

package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.ExpenseRequestDto;
import com.expense.ExpenseTracker.dto.ExpenseResponseDto;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDto> create(@RequestBody @Valid ExpenseRequestDto newExpenseDto) {
        Expense expense = new Expense(newExpenseDto.getDescription(), newExpenseDto.getAmount());
        Expense savedExpense = expenseService.addNew(expense);
        return new ResponseEntity(new ExpenseResponseDto(savedExpense.getId(), savedExpense.getDescription(), savedExpense.getAmount()), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDto>> getAll() {
        List<Expense> expenses = expenseService.getAll();
        List<ExpenseResponseDto> expenseDtos = new ArrayList<>();
        for(Expense expense : expenses) {
            expenseDtos.add(new ExpenseResponseDto(expense.getId(), expense.getDescription(), expense.getAmount()));
        }
        return ResponseEntity.ok(expenseDtos);
    }

    @GetMapping("{id}")
    public ResponseEntity<ExpenseResponseDto> getById(@PathVariable Long id) throws NotFoundException {
        Expense expense = expenseService.getById(id);
        return ResponseEntity.ok(new ExpenseResponseDto(expense.getId(), expense.getDescription(), expense.getAmount()));
    }

    @PutMapping("{id}")
    public ResponseEntity<ExpenseResponseDto> update(@PathVariable Long id, @RequestBody @Valid ExpenseRequestDto updateDto) throws NotFoundException {
        Expense updatedExpense = expenseService.update(id, updateDto);
        return ResponseEntity.ok(new ExpenseResponseDto(updatedExpense.getId(), updatedExpense.getDescription(), updatedExpense.getAmount()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Long id) throws NotFoundException {
        expenseService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}

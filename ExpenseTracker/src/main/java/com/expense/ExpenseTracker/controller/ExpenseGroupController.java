package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.ExpenseGroupRequestDto;
import com.expense.ExpenseTracker.dto.ExpenseGroupResponseDto;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.ExpenseGroup;
import com.expense.ExpenseTracker.service.ExpenseGroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/expense-groups")
public class ExpenseGroupController {

    private final ExpenseGroupService expenseGroupService;

    public ExpenseGroupController(ExpenseGroupService expenseGroupService) {
        this.expenseGroupService = expenseGroupService;
    }

    @PostMapping
    public ResponseEntity<ExpenseGroupResponseDto> create(@RequestBody @Valid ExpenseGroupRequestDto newExpenseGroupDto) {
        ExpenseGroup expenseGroup = new ExpenseGroup(newExpenseGroupDto.getName(), newExpenseGroupDto.getDescription());
        ExpenseGroup savedExpenseGroup = expenseGroupService.addNew(expenseGroup);
        return new ResponseEntity(new ExpenseGroupResponseDto(savedExpenseGroup.getId(), savedExpenseGroup.getName(), savedExpenseGroup.getDescription()), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseGroupResponseDto>> getAll() {
        List<ExpenseGroup> expenseGroups = expenseGroupService.getAll();
        List<ExpenseGroupResponseDto> expenseGroupDtos = new ArrayList<>();
        for(ExpenseGroup expenseGroup : expenseGroups) {
            expenseGroupDtos.add(new ExpenseGroupResponseDto(expenseGroup.getId(), expenseGroup.getName(), expenseGroup.getDescription()));
        }
        return ResponseEntity.ok(expenseGroupDtos);
    }

    @GetMapping("{id}")
    public ResponseEntity<ExpenseGroupResponseDto> getById(@PathVariable Long id) {
        Optional<ExpenseGroup> expenseGroup = expenseGroupService.getById(id);
        if(expenseGroup.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new ExpenseGroupResponseDto(expenseGroup.get().getId(), expenseGroup.get().getName(), expenseGroup.get().getDescription()));
    }

    @PutMapping("{id}")
    public ResponseEntity<ExpenseGroupResponseDto> update(@PathVariable Long id, @RequestBody @Valid ExpenseGroupRequestDto updateDto) {
        try {
            ExpenseGroup updatedExpenseGroup = expenseGroupService.update(id, updateDto);
            return ResponseEntity.ok(new ExpenseGroupResponseDto(updatedExpenseGroup.getId(), updatedExpenseGroup.getName(), updatedExpenseGroup.getDescription()));
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            expenseGroupService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}

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
    public ResponseEntity<ExpenseGroupResponseDto> getById(@PathVariable Long id) throws NotFoundException {
        ExpenseGroup expenseGroup = expenseGroupService.getById(id);
        return ResponseEntity.ok(new ExpenseGroupResponseDto(expenseGroup.getId(), expenseGroup.getName(), expenseGroup.getDescription()));
    }

    @PutMapping("{id}")
    public ResponseEntity<ExpenseGroupResponseDto> update(@PathVariable Long id, @RequestBody @Valid ExpenseGroupRequestDto updateDto) throws NotFoundException {
        ExpenseGroup updatedExpenseGroup = expenseGroupService.update(id, updateDto);
        return ResponseEntity.ok(new ExpenseGroupResponseDto(updatedExpenseGroup.getId(), updatedExpenseGroup.getName(), updatedExpenseGroup.getDescription()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Long id) throws NotFoundException {
        expenseGroupService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

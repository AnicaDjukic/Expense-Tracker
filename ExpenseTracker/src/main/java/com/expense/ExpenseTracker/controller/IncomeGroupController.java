package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.ExpenseGroupRequestDto;
import com.expense.ExpenseTracker.dto.IncomeGroupRequestDto;
import com.expense.ExpenseTracker.dto.IncomeGroupResponseDto;
import com.expense.ExpenseTracker.exception.NotFoundException;
import com.expense.ExpenseTracker.model.IncomeGroup;
import com.expense.ExpenseTracker.service.IncomeGroupService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/income-groups")
public class IncomeGroupController {

    private final IncomeGroupService incomeGroupService;

    private final ModelMapper modelMapper = new ModelMapper();

    public IncomeGroupController(IncomeGroupService incomeGroupService) {
        this.incomeGroupService = incomeGroupService;
    }

    @PostMapping
    public ResponseEntity<IncomeGroupResponseDto> create(@RequestBody @Valid IncomeGroupRequestDto newExpenseGroupDto) {
        IncomeGroup expenseGroup = modelMapper.map(newExpenseGroupDto, IncomeGroup.class);
        IncomeGroup savedIncomeGroup = incomeGroupService.addNew(expenseGroup);
        return new ResponseEntity(modelMapper.map(savedIncomeGroup, IncomeGroupResponseDto.class), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<IncomeGroupResponseDto>> getAll() {
        List<IncomeGroup> incomeGroups = incomeGroupService.getAll();
        List<IncomeGroupResponseDto> incomeGroupDtos = new ArrayList<>();
        for(IncomeGroup expenseGroup : incomeGroups) {
            incomeGroupDtos.add(modelMapper.map(expenseGroup, IncomeGroupResponseDto.class));
        }
        return ResponseEntity.ok(incomeGroupDtos);
    }

    @GetMapping("{id}")
    public ResponseEntity<IncomeGroupResponseDto> getById(@PathVariable UUID id) throws NotFoundException {
        IncomeGroup incomeGroup = incomeGroupService.getById(id);
        return ResponseEntity.ok(modelMapper.map(incomeGroup, IncomeGroupResponseDto.class));
    }

    @PutMapping("{id}")
    public ResponseEntity<IncomeGroupResponseDto> update(@PathVariable UUID id, @RequestBody @Valid ExpenseGroupRequestDto updateDto) throws NotFoundException {
        IncomeGroup updatedIncomeGroup = incomeGroupService.update(id, updateDto);
        return ResponseEntity.ok(modelMapper.map(updatedIncomeGroup, IncomeGroupResponseDto.class));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable UUID id) throws NotFoundException {
        incomeGroupService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}

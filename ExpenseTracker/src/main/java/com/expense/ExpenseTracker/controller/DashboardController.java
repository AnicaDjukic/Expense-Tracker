package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.DashboardDto;
import com.expense.ExpenseTracker.dto.ExpenseResponseDto;
import com.expense.ExpenseTracker.dto.IncomeResponseDto;
import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.model.Income;
import com.expense.ExpenseTracker.service.DashboardService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    private final ModelMapper modelMapper = new ModelMapper();

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/{size}")
    @ResponseStatus(value = HttpStatus.OK)
    public DashboardDto getDashboard(@PathVariable int size) {
        double totalAmount = dashboardService.getTotalAmount();
        List<Expense> expenses = dashboardService.getLastFewExpenses(size);
        List<Income> incomes = dashboardService.getLastFewIncomes(size);
        return createDashboard(totalAmount, expenses, incomes);
    }

    private DashboardDto createDashboard(double totalAmount, List<Expense> expenses, List<Income> incomes) {
        List<ExpenseResponseDto> expenseDtos = new ArrayList<>();
        for (Expense expense : expenses)
            expenseDtos.add(modelMapper.map(expense, ExpenseResponseDto.class));
        List<IncomeResponseDto> incomeDtos = new ArrayList<>();
        for(Income income : incomes)
            incomeDtos.add(modelMapper.map(income, IncomeResponseDto.class));
        return new DashboardDto(totalAmount, expenseDtos, incomeDtos);
    }
}

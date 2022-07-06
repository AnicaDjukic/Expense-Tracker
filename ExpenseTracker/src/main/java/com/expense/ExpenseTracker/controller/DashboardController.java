package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.DashboardDto;
import com.expense.ExpenseTracker.dto.ExpenseResponseDto;
import com.expense.ExpenseTracker.dto.IncomeResponseDto;
import com.expense.ExpenseTracker.model.Expense;
import com.expense.ExpenseTracker.model.Income;
import com.expense.ExpenseTracker.security.SecurityUtil;
import com.expense.ExpenseTracker.service.DashboardService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    private final ModelMapper modelMapper = new ModelMapper();

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public DashboardDto getDashboard(@RequestParam(required = false, defaultValue = "5") int size) {
        String username = SecurityUtil.getLoggedIn().getName();
        double totalAmount = dashboardService.getTotalAmount(username);
        List<Expense> expenses = dashboardService.getLastFewExpenses(size, username);
        List<Income> incomes = dashboardService.getLastFewIncomes(size, username);
        return createDashboard(totalAmount, expenses, incomes);
    }

    private DashboardDto createDashboard(double totalAmount, List<Expense> expenses, List<Income> incomes) {
        List<ExpenseResponseDto> expenseDtos = expenses.stream().map(expense -> modelMapper
                .map(expense, ExpenseResponseDto.class)).collect(Collectors.toList());
        List<IncomeResponseDto> incomeDtos = incomes.stream().map(income -> modelMapper
                .map(income, IncomeResponseDto.class)).collect(Collectors.toList());
        return new DashboardDto(totalAmount, expenseDtos, incomeDtos);
    }
}

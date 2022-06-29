package com.expense.ExpenseTracker.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String resource) {
        super(resource + " not found!");
    }
}

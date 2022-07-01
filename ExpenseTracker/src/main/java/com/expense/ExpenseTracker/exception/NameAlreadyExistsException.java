package com.expense.ExpenseTracker.exception;

public class NameAlreadyExistsException extends RuntimeException {

    public NameAlreadyExistsException(String resource, String name) {
        super(resource + " with name: " + name + " already exists!");
    }
}

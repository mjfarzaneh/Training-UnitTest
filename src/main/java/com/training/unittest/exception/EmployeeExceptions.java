package com.training.unittest.exception;

public class EmployeeExceptions extends RuntimeException {
    public EmployeeExceptions(String message) {
        super(message);
    }

    public EmployeeExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}

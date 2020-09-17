package com.chronos.chronosserver.exception;

public class OperationForbiddenException extends Exception {
    public OperationForbiddenException() {
        super();
    }

    public OperationForbiddenException(String message) {
        super(message);
    }
}

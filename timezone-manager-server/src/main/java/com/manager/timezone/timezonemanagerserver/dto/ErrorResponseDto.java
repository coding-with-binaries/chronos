package com.manager.timezone.timezonemanagerserver.dto;

import org.springframework.http.HttpStatus;

public class ErrorResponseDto {
    private int statusCode;

    private String status;

    private String message;

    public ErrorResponseDto(HttpStatus httpStatus, String message) {
        this.statusCode = httpStatus.value();
        this.status = httpStatus.name();
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

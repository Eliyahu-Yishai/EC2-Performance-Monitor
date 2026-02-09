package com.aws_monitor.common.errors.dto;

public class ApiErrorResponse {

    private final String code;
    private final String message;

    public ApiErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

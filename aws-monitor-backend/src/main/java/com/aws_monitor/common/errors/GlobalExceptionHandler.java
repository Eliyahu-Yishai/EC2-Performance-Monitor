package com.aws_monitor.common.errors;

import com.aws_monitor.common.errors.dto.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse> validationError(ValidationException ex) {
        return ResponseEntity.status(400)
                .body(new ApiErrorResponse("BAD_REQUEST", ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> notFound(NotFoundException ex) {
        return ResponseEntity.status(404)
                .body(new ApiErrorResponse("NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(TerminationNotAllowedException.class)
    public ResponseEntity<ApiErrorResponse> terminationNotAllowed(TerminationNotAllowedException ex) {
        return ResponseEntity.status(403)
                .body(new ApiErrorResponse("FORBIDDEN", ex.getMessage()));
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiErrorResponse> applicationError(ApplicationException ex) {
        return ResponseEntity.status(500)
                .body(new ApiErrorResponse("INTERNAL_ERROR", "Unexpected error"));
    }
}

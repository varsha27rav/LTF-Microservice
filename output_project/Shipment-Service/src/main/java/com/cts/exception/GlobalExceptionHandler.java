package com.cts.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ 1. Validation Errors (DTO validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));

        Map<String, Object> response = new HashMap<>();
        response.put("time", LocalDateTime.now());
        response.put("status", 400);
        response.put("errors", errors);

        return response;
    }

    // ✅ 2. Invalid Status Exception
    @ExceptionHandler(InvalidStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleInvalidStatus(InvalidStatusException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("time", LocalDateTime.now());
        response.put("status", 400);
        response.put("message", ex.getMessage());

        return response;
    }

    // ✅ 3. Resource Not Found (Business Exception)
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFound(RuntimeException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("time", LocalDateTime.now());
        response.put("status", 404);
        response.put("message", ex.getMessage());

        return response;
    }

    // ✅ 4. Generic Exception (MUST be last)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGeneric(Exception ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("time", LocalDateTime.now());
        response.put("status", 500);
        response.put("message", "Something went wrong");

        return response;
    }
}
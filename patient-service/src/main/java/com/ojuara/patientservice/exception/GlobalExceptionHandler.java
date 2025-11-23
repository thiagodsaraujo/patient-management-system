package com.ojuara.patientservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>>
    handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex) {

        log.warn("Email already exists {}.", ex.getMessage());

        Map<String, String> error = new HashMap<>();
        error.put("message", "Email already exists");
        return ResponseEntity.badRequest().body(error);

    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<Map<String, String>>
    handlePatientNotFoundException(
            PatientNotFoundException ex) {

        log.warn("Patient not found: {}.", ex.getMessage());

        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());

        // O status 404 Not Found é mais apropriado para recursos não encontrados
        return ResponseEntity.status(404).body(error);

    }

}

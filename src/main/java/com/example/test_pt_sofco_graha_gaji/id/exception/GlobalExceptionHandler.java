package com.example.test_pt_sofco_graha_gaji.id.exception;

import com.example.test_pt_sofco_graha_gaji.id.dto.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WebResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        WebResponse<Object> response = WebResponse.builder()
                .status("error")
                .message("Validation failed")
                .errors(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<WebResponse<Object>> handleApiException(ApiException ex) {
        WebResponse<Object> response = WebResponse.builder()
                .status("error")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<WebResponse<Object>> handleDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException ex) {
        WebResponse<Object> response = WebResponse.builder()
                .status("error")
                .message("Database integrity violation: " + ex.getMostSpecificCause().getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<Object>> handleResponseStatusException(ResponseStatusException ex) {
        WebResponse<Object> response = WebResponse.builder()
                .status("error")
                .message(ex.getReason())
                .build();

        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebResponse<Object>> handleGeneralException(Exception ex) {
        WebResponse<Object> response = WebResponse.builder()
                .status("error")
                .message("An unexpected error occurred: " + ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

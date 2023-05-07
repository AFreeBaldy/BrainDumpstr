package com.capedbaldy.braindumpstr.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
@RestControllerAdvice
public class DumpsterServiceErrorHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleInvalidDumpId(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(DumpDoesNotExistException.class)
    public ResponseEntity<?> handleDumpIdDoesntExist(DumpDoesNotExistException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}

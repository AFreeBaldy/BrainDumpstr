package com.capedbaldy.braindumpstr.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class DumpsterServiceErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidDumpIdException.class)
    public ResponseEntity<?> handleInvaliddumpId(InvalidDumpIdException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}

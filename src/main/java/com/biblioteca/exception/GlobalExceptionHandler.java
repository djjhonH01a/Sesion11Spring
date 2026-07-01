package com.biblioteca.exception;

import com.biblioteca.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LibroNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLibroNotFound(LibroNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 404, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioNotFound(UsuarioNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 404, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(PrestamoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePrestamoNotFound(PrestamoNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 404, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(LibroNoDisponibleException.class)
    public ResponseEntity<ErrorResponse> handleLibroNoDisponible(LibroNoDisponibleException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 409, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}

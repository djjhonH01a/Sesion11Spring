package com.biblioteca.exception;

public class PrestamoNotFoundException extends RuntimeException {

    public PrestamoNotFoundException(Long id) {
        super("Préstamo no encontrado con id: " + id);
    }
}

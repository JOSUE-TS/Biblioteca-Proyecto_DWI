package com.example.biblioteca.controlador;

import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejadorErrores {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> duplicado() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of("error", "Registro repetido o relacionado con otros datos"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> datoIncorrecto() {
        return ResponseEntity.badRequest().body(Map.of("error", "Revisa los campos obligatorios"));
    }
}

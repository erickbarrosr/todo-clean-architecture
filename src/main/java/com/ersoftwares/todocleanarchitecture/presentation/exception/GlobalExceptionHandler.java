package com.ersoftwares.todocleanarchitecture.presentation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Tratamento centralizado de exceções para toda a API.
 *
 * @RestControllerAdvice intercepta exceções lançadas em qualquer @RestController
 * e permite retornar respostas HTTP padronizadas em vez de stack traces.
 * <p>
 * Sem isso, uma IllegalArgumentException do use case retornaria 500.
 * Com isso, retornamos 400 com uma mensagem amigável.
 * <p>
 * Separa o tratamento de erro da lógica do controller — cada um no seu lugar.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura erros de validação do Bean Validation (@NotBlank, @Size...).
     * Lançado quando @Valid falha no controller.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        // Pega o primeiro erro de validação e retorna a mensagem.
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .orElse("Dados inválidos");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("erro", message));
    }

    /**
     * Captura erros de "não encontrado" ou dados inválidos dos use cases.
     * IllegalArgumentException é convenção para "entrada inválida".
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(
            IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("erro", ex.getMessage()));
    }

    /**
     * Captura erros de estado inválido do domínio.
     * IllegalStateException é convenção para "operação não permitida no estado atual".
     * Ex: tentar completar uma tarefa já completa.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(
            IllegalStateException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)  // 409 Conflict: estado inconsistente
                .body(Map.of("erro", ex.getMessage()));
    }
}

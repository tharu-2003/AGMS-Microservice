package lk.ijse.auth_service.util;

import lk.ijse.auth_service.dto.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class acts as a central "Safety Net" for the Auth Service.
 * It catches any errors (exceptions) that happen in the controllers or services
 * and sends a clean, readable message back to the user instead of a scary technical error.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Catches generic business logic errors.
     * Example: "Username already exists" or "Invalid password".
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MessageResponse> handleRuntime(RuntimeException ex) {
        // Return a 400 Bad Request status with the specific error message
        return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
    }

    /**
     * Catches input validation errors.
     * This happens when a user sends data that doesn't follow our rules
     * (e.g., leaving a required field empty).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidation(MethodArgumentNotValidException ex) {
        // 1. Try to find the specific message we defined in the DTO (like "Password is required")
        String message = ex.getBindingResult().getFieldError() != null
                ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : "Validation failed";

        // 2. Return a 400 Bad Request status with that specific message
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(message));
    }
}
package lk.ijse.zone_service.util;

import lk.ijse.zone_service.dto.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class acts as a global "Safety Net" for the Zone Service.
 * It intercepts any errors (exceptions) that occur within the service and
 * converts them into clean, professional JSON responses for the user.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. Handles Business Logic Errors.
     * This catches errors thrown manually in our service layer.
     * Example: "Min temperature must be less than max temperature"
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MessageResponse> handleRuntime(RuntimeException ex) {
        // Returns a 400 Bad Request status with the specific logic error message
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(ex.getMessage()));
    }

    /**
     * 2. Handles Input Validation Errors.
     * This catches errors triggered by @Valid annotations in our DTOs.
     * It ensures the data sent by the user follows the required format.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidation(MethodArgumentNotValidException ex) {
        // Get the specific error message defined in the DTO, otherwise use a default
        String message = ex.getBindingResult().getFieldError() != null
                ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : "Validation failed";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(message));
    }

    /**
     * 3. Handles Unexpected System Errors (Catch-All).
     * This ensures that even if a serious crash occurs (like database failure),
     * the user sees a polite message instead of a technical Java error page.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleGeneralException(Exception ex) {
        // Returns a 500 Internal Server Error status
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("An unexpected internal error occurred."));
    }
}
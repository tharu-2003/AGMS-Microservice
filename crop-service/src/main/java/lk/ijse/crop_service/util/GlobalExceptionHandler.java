package lk.ijse.crop_service.util;

import lk.ijse.crop_service.dto.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class acts as a central "Safety Net" for the entire Crop Service.
 * It catches any errors (exceptions) that happen during execution and sends
 * back a clean JSON response instead of a technical error page.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Catches errors related to the system's logic.
     * In this service, it is primarily used to catch "State Machine" errors,
     * such as trying to move a crop stage in the wrong order.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MessageResponse> handleRuntime(RuntimeException ex) {
        // Returns a 400 Bad Request with the specific error message (e.g., "Invalid Step")
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(ex.getMessage()));
    }

    /**
     * Catches data validation errors.
     * This happens if the user sends a request body that is missing required fields
     * or contains data in the wrong format.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidation(MethodArgumentNotValidException ex) {
        // Get the specific validation message we wrote in the DTO
        String message = ex.getBindingResult().getFieldError() != null
                ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : "Validation failed";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(message));
    }

    /**
     * A catch-all handler for any other unexpected errors.
     * This ensures the user sees a polite message if something like a
     * database connection failure occurs.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleGeneralException(Exception ex) {
        // Returns a 500 Internal Server Error status
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("An unexpected internal error occurred."));
    }
}
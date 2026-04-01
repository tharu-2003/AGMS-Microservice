package lk.ijse.automation_service.util;

import lk.ijse.automation_service.dto.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class acts as a central "Safety Net" for the entire Automation Service.
 * It catches any errors (exceptions) that happen during processing and
 * sends back a clean, professional response instead of a technical crash report.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Catches common errors related to business logic or unexpected code issues.
     * For example: If the Zone Service is down or a Zone ID cannot be parsed.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MessageResponse> handleRuntime(RuntimeException ex) {
        // Sends a 400 Bad Request status with the specific error message
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(ex.getMessage()));
    }

    /**
     * Catches validation errors.
     * This happens when the data sent to an API does not follow the required format
     * (e.g., if a required field like 'zoneId' is missing in the request body).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidation(MethodArgumentNotValidException ex) {
        // Try to get the specific message we defined in the DTO, otherwise use a default
        String message = ex.getBindingResult().getFieldError() != null
                ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : "Validation failed";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(message));
    }

    /**
     * This is the final catch-all handler for any other unexpected errors.
     * It ensures that even if something goes seriously wrong, the user receives
     * a polite error message instead of a long, confusing Java stack trace.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleGeneralException(Exception ex) {
        // Returns a 500 Internal Server Error status
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("An unexpected internal error occurred."));
    }
}
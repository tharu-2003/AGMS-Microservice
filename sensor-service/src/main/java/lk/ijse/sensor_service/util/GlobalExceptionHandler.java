package lk.ijse.sensor_service.util;

import lk.ijse.sensor_service.dto.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class acts as a central "Safety Net" for the entire Sensor Service.
 * It catches any errors (exceptions) that happen during data fetching or
 * processing and sends back a clean, professional response.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Catches common business logic errors or manual exceptions.
     * For example: If no sensor data is found when calling the /latest endpoint.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MessageResponse> handleRuntime(RuntimeException ex) {
        // Returns a 400 Bad Request status with the specific error message
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(ex.getMessage()));
    }

    /**
     * Catches validation errors in incoming requests.
     * This triggers if the user sends data that does not follow the rules defined in our DTOs.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidation(MethodArgumentNotValidException ex) {
        // Try to get the specific error message defined in the DTO, otherwise use a default
        String message = ex.getBindingResult().getFieldError() != null
                ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : "Validation failed";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(message));
    }

    /**
     * A catch-all handler for any other unexpected system errors.
     * It ensures that even if something goes wrong (like a database failure),
     * the user sees a polite message instead of a long technical error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleGeneralException(Exception ex) {
        // Returns a 500 Internal Server Error status
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("An unexpected internal error occurred."));
    }
}
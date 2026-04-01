package lk.ijse.auth_service.controller;

import jakarta.validation.Valid;
import lk.ijse.auth_service.dto.*;
import lk.ijse.auth_service.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This controller handles all web requests related to user authentication.
 * It provides endpoints for signing up, logging in, and managing tokens.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // Injecting the AuthService to handle the business logic
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint to register a new user in the system.
     * It validates the input data and returns a success message.
     */
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) {
        // Calls the service to save the user and returns 200 OK
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Endpoint for user login.
     * It checks if the username and password are correct.
     * If successful, it returns a JWT Access Token and a Refresh Token.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        // Calls the service to verify credentials and generate tokens
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Endpoint to get a new Access Token.
     * Users call this when their current Access Token expires,
     * using the Refresh Token provided during login.
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        // Calls the service to validate the refresh token and issue a new access token
        return ResponseEntity.ok(authService.refresh(request));
    }
}
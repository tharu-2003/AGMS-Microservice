package lk.ijse.auth_service.service;

import lk.ijse.auth_service.dto.*;
import lk.ijse.auth_service.entity.RefreshToken;
import lk.ijse.auth_service.entity.User;
import lk.ijse.auth_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * This service contains the core logic for the Authentication system.
 * It handles how users sign up, how they log in, and how they stay logged in.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * Handles new user registration.
     * It checks if the name is already taken and encrypts the password for safety.
     */
    public MessageResponse register(RegisterRequest request) {
        // 1. Check if the username already exists in the database
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // 2. Create a new User object and encode the plain-text password
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())) // Security: Never save plain passwords
                .role(request.getRole())
                .build();

        // 3. Save the user to the database
        userRepository.save(user);
        return new MessageResponse("User registered successfully");
    }

    /**
     * Handles the login process.
     * It verifies the identity of the user and issues security tokens.
     */
    public AuthResponse login(LoginRequest request) {
        // 1. Find the user by their username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Compare the provided password with the encrypted password in the database
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // 3. If credentials are correct, generate a short-term Access Token
        String accessToken = jwtService.generateAccessToken(user);

        // 4. Generate a long-term Refresh Token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        // 5. Return the tokens and user details to the client
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    /**
     * Handles the request for a new Access Token using a Refresh Token.
     */
    public AuthResponse refresh(RefreshTokenRequest request) {
        // 1. Verify if the provided refresh token is still valid
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
        User user = refreshToken.getUser();

        // 2. Generate a fresh Access Token for the user
        String newAccessToken = jwtService.generateAccessToken(user);

        // 3. Return the new token along with existing session info
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
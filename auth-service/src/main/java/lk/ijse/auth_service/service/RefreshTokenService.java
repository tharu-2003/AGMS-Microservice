package lk.ijse.auth_service.service;

import jakarta.transaction.Transactional;
import lk.ijse.auth_service.entity.RefreshToken;
import lk.ijse.auth_service.entity.User;
import lk.ijse.auth_service.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * This service manages "Refresh Tokens".
 * Refresh tokens are used to keep a user logged in for a long time (e.g., 7 days)
 * so they don't have to type their password every time the short-term Access Token expires.
 */
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // How long the refresh token is valid (loaded from configuration)
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Creates a brand new Refresh Token for a user.
     * It deletes any old tokens for this user first to keep the database clean.
     */
    @Transactional
    public RefreshToken createRefreshToken(User user) {
        // 1. Remove old refresh tokens associated with this user
        refreshTokenRepository.deleteByUser(user);

        // 2. Build a new token object with a unique random ID (UUID)
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString()) // Generates a unique string
                .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000))
                .revoked(false) // Token is active by default
                .build();

        // 3. Save and return the new token
        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Verifies if a given refresh token string is actually valid and safe to use.
     */
    public RefreshToken verifyRefreshToken(String token) {
        // 1. Try to find the token in the database
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token: Not found in system"));

        // 2. Check if the token was manually cancelled (revoked) for security reasons
        if (refreshToken.isRevoked()) {
            throw new RuntimeException("Refresh token has been revoked and cannot be used");
        }

        // 3. Check if the token has passed its expiration date
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token has expired. Please log in again.");
        }

        // If all checks pass, return the token object
        return refreshToken;
    }
}
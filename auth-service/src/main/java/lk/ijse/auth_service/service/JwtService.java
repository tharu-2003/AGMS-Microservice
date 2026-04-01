package lk.ijse.auth_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lk.ijse.auth_service.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This service is responsible for handling all JWT (JSON Web Token) operations.
 * It handles token creation, parsing (reading), and validation.
 */
@Service
public class JwtService {

    // The secret key used to sign and verify tokens (loaded from Config Server)
    @Value("${jwt.secret}")
    private String secretKey;

    // How long the token remains valid (usually 15 minutes)
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    /**
     * Generates a new JWT Access Token for a specific user.
     */
    public String generateAccessToken(User user) {
        // Extra data we want to put inside the token (like the user's role)
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());

        return Jwts.builder()
                .setClaims(claims) // Add the extra data
                .setSubject(user.getUsername()) // Set the username as the main identity
                .setIssuedAt(new Date(System.currentTimeMillis())) // Token creation time
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration)) // Expiry time
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Sign the token for security
                .compact(); // Finalize and return the token string
    }

    /**
     * Helper method to get the username from a token string.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Checks if a token belongs to the correct user and has not expired.
     */
    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Checks if the token's expiration date has passed.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Gets the expiration date from the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * A generic helper method to extract specific information (claims) from the token.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * This is the "brain" of the parsing logic.
     * it reads the token and verifies its signature using our secret key.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()) // Use the secret key to verify the token
                .build()
                .parseClaimsJws(token)
                .getBody(); // Return the data payload of the token
    }

    /**
     * Converts the Base64 secret string into a cryptographic key.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
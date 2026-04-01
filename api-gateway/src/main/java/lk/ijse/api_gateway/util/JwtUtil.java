package lk.ijse.api_gateway.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

<<<<<<< Updated upstream
=======
import javax.crypto.SecretKey;

/**
 * This utility class is responsible for verifying the technical details of a JWT token.
 * It checks if the token was actually created by our system and if it is still valid.
 */
>>>>>>> Stashed changes
@Component
public class JwtUtil {

    // The secret key used to sign the tokens, loaded from the configuration file (Config Server)
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Checks if the provided token is valid.
     * @param token The JWT string received from the user.
     * @return true if the token is valid, false if it is expired or fake.
     */
    public boolean isValid(String token) {

        try {
<<<<<<< Updated upstream

            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token);

=======
            // Use the JJWT library to parse (read) the token
            Jwts.parserBuilder()
                    .setSigningKey(getSignInKey()) // Use our secret key to check the signature
                    .build()
                    .parseClaimsJws(token); // If this line doesn't throw an error, the token is valid
>>>>>>> Stashed changes
            return true;

        } catch (Exception e) {
<<<<<<< Updated upstream

=======
            // If the token is expired, tempered with, or wrong, it will come here
            System.out.println("JWT validation failed: " + e.getMessage());
>>>>>>> Stashed changes
            return false;

        }
    }
<<<<<<< Updated upstream
=======

    /**
     * Converts the Base64 encoded secret string into a real cryptographic Key object.
     */
    private SecretKey getSignInKey() {
        // 1. Decode the secret key from Base64 format
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // 2. Turn the bytes into a SecretKey object that the JJWT library understands
        return Keys.hmacShaKeyFor(keyBytes);
    }
>>>>>>> Stashed changes
}
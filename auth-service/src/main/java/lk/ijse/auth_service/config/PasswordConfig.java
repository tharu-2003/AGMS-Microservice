package lk.ijse.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class tells the system how to handle user passwords.
 * We use it to make sure passwords are hidden (encrypted) for security.
 */
@Configuration
public class PasswordConfig {

    /**
     * This method creates a "Bean" or a tool that Spring can use
     * to encode passwords before saving them to the database.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // We use BCrypt, which is a strong industry-standard algorithm
        // to turn plain-text passwords into a secure hash.
        return new BCryptPasswordEncoder();
    }
}
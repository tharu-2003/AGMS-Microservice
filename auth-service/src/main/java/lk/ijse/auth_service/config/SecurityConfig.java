package lk.ijse.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This class defines the security rules for the Auth Service.
 * It determines which endpoints are public and which are protected.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF (Cross-Site Request Forgery) protection.
                // We do this because we are building a stateless REST API that uses tokens.
                .csrf(csrf -> csrf.disable())

                // 2. Set the session management to "Stateless".
                // This means the server won't store user information in cookies or sessions.
                // Instead, every request must be authenticated using a JWT token.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Define access rules for different URL paths.
                .authorizeHttpRequests(auth -> auth
                        // Allow everyone to access Login, Register, and Health check endpoints
                        .requestMatchers("/api/auth/**", "/actuator/**").permitAll()

                        // Any other request that is not listed above must be authenticated (logged in)
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
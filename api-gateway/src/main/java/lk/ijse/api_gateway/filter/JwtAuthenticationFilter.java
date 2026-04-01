package lk.ijse.api_gateway.filter;

import lk.ijse.api_gateway.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * This class acts as a security guard for the entire system.
 * It checks every request coming through the API Gateway to ensure the user has a valid token.
 */
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
<<<<<<< Updated upstream

=======
        // 1. Get the URL path of the incoming request
        String path = exchange.getRequest().getURI().getPath();
        System.out.println("Processing request for path: " + path);

        // 2. Skip security check for 'OPTIONS' requests (used for CORS pre-flight)
        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        // 3. Skip security check for public endpoints (Login and Register)
        if (isOpenEndpoint(path)) {
            System.out.println("Allowing access to public endpoint: " + path);
            return chain.filter(exchange);
        }

        // 4. Check if the 'Authorization' header exists in the request
>>>>>>> Stashed changes
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // 5. If header is missing or does not start with 'Bearer ', block the request
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
<<<<<<< Updated upstream

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();

=======
            System.out.println("Access Denied: No valid Authorization header found");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete(); // Stop the request here
>>>>>>> Stashed changes
        }

        // 6. Extract the actual token from the header (remove 'Bearer ' prefix)
        String token = authHeader.substring(7);

        // 7. Use JwtUtil to check if the token is valid (not expired or tempered)
        if (!jwtUtil.isValid(token)) {
<<<<<<< Updated upstream

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();

        }

        return chain.filter(exchange);
    }

=======
            System.out.println("Access Denied: The provided token is invalid");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete(); // Stop the request here
        }

        // 8. If everything is fine, allow the request to proceed to the microservice
        System.out.println("Access Granted: Token is valid");
        return chain.filter(exchange);
    }

    /**
     * Defines endpoints that do not require a token (Login/Register).
     */
    private boolean isOpenEndpoint(String path) {
        return path.startsWith("/api/auth/");
    }

    /**
     * Sets the priority of this filter.
     * -1 means it runs before other filters to ensure security is checked first.
     */
>>>>>>> Stashed changes
    @Override
    public int getOrder() {
        return -1;
    }
}
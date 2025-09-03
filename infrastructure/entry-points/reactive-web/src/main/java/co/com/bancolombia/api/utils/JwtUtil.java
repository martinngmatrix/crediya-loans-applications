package co.com.bancolombia.api.utils;

import org.springframework.web.reactive.function.server.ServerRequest;

public class JwtUtil {
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private JwtUtil() {}

    public static String extractToken(ServerRequest request) {
        String authHeader = request.headers().firstHeader(AUTH_HEADER);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }

        return null;
    }    
}

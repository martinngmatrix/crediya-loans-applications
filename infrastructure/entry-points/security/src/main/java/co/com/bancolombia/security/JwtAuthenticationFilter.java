package co.com.bancolombia.security;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import co.com.bancolombia.utils.JwtService;

@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        List<String> whitelist = List.of(
                "/api/v1/login",
                "/swagger-ui.html",
                "/swagger-ui/",
                "/swagger-ui",
                "/swagger-ui/**",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/webjars/",
                "/webjars/**",
                "/configuration/ui",
                "/configuration/security",
                "/actuator/",
                "/actuator",
                "/actuator/health/",
                "/actuator/health",
                "/health/",
                "/health"
        );
        String path = exchange.getRequest().getPath().value();

        if (whitelist.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            return jwtService.validateToken(token)
                    .flatMap(valid -> {
                        if (valid) {
                            return jwtService.getRoleFromToken(token)
                                    .flatMap(role -> {
                                        exchange.getAttributes().put("role", role);
                                        return chain.filter(exchange);
                                    });
                        } else {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }
                    })
                    .onErrorResume(e -> {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        }

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}

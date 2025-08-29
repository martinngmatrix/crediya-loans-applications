package co.com.bancolombia.security;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

public class RoleAuthorizationFilter implements WebFilter {

    private final Map<String, List<String>> routeRoles = Map.of(
            "/api/v1/solicitud", List.of("cliente")
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        String role = (String) exchange.getAttribute("role");

        for (Map.Entry<String, List<String>> entry : routeRoles.entrySet()) {
            if (path.startsWith(entry.getKey())) {
                if (role == null || !entry.getValue().contains(role)) {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }
        }

        return chain.filter(exchange);
    }
}

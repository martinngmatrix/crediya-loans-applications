package co.com.bancolombia.security;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

public class RoleAuthorizationFilter implements WebFilter {

    private final Map<String, Map<String, List<String>>> routeRoles = Map.of(
        "/api/v1/solicitud", Map.of(
            "GET", List.of("admin", "asesor"),
            "POST", List.of("cliente"),
            "PUT", List.of("admin", "asesor")
        ),
        "/api/v1/calcular-capacidad", Map.of(
            "POST", List.of("admin")
        )
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethod() != null
                ? exchange.getRequest().getMethod().name()
                : "";
        String role = (String) exchange.getAttribute("role");

        for (Map.Entry<String, Map<String, List<String>>> routeEntry : routeRoles.entrySet()) {
            if (path.startsWith(routeEntry.getKey())) {
                Map<String, List<String>> methodRoles = routeEntry.getValue();
                List<String> allowedRoles = methodRoles.get(method);

                if (allowedRoles == null || allowedRoles.isEmpty()) {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }

                if (role == null || !allowedRoles.contains(role)) {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }
        }

        return chain.filter(exchange);
    }
}

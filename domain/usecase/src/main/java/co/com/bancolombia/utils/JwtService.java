package co.com.bancolombia.utils;

import co.com.bancolombia.model.user.User;
import reactor.core.publisher.Mono;

public interface JwtService {
    Mono<String> generateToken(User user);
    Mono<Boolean> validateToken(String token);
    Mono<String> getRoleFromToken(String token);
}
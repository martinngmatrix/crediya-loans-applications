package co.com.bancolombia.model.user.gateways;

import java.math.BigInteger;

import co.com.bancolombia.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> findByDocumentNumber(String token, String documentNumber);
    Mono<User> findById(String token, BigInteger id);
}

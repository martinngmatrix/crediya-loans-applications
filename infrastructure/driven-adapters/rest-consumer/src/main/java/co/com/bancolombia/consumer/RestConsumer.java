package co.com.bancolombia.consumer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;

@Service
@RequiredArgsConstructor
public class RestConsumer implements UserRepository{
    private final WebClient client;

    @Override
    @CircuitBreaker(name = "testGet")
    public Mono<User> findById(String token, BigInteger id) {
    return client.get()
            .uri("/usuarios/{id}", id)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToMono(User.class);
    }

    @Override
    @CircuitBreaker(name = "testGet")
    public Mono<User> findByDocumentNumber(String token, String documentNumber) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/usuarios")
                        .queryParam("document", documentNumber)
                        .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(User.class);
    }
}

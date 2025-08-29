package co.com.bancolombia.model.user.gateway;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class UserRepositoryTest {

    @Test
    void testFindByDocumentNumberFound() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        User expectedUser = User.builder()
                .name("Carlos")
                .lastName("Ramirez")
                .dateOfBirth(LocalDate.of(1992, 3, 10))
                .address("Jr. Los Olivos 321")
                .email("carlos@example.com")
                .phone("111222333")
                .baseSalary(BigDecimal.valueOf(3500))
                .documentNumber("11223344")
                .build();

        when(userRepository.findByDocumentNumber(eq("11223344")))
                .thenReturn(Mono.just(expectedUser));

        Mono<User> result = userRepository.findByDocumentNumber("11223344");

        StepVerifier.create(result)
                .expectNext(expectedUser)
                .verifyComplete();

        verify(userRepository, times(1)).findByDocumentNumber("11223344");
    }

    @Test
    void testFindByDocumentNumberNotFound() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        when(userRepository.findByDocumentNumber(eq("00000000")))
                .thenReturn(Mono.empty());

        Mono<User> result = userRepository.findByDocumentNumber("00000000");

        StepVerifier.create(result)
                .verifyComplete();

        verify(userRepository, times(1)).findByDocumentNumber("00000000");
    }
}

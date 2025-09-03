package co.com.bancolombia.model.user.gateways;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class UserRepositoryTest {
    String token = "example_token";
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

        when(userRepository.findByDocumentNumber(eq(token), eq("11223344")))
                .thenReturn(Mono.just(expectedUser));

        Mono<User> result = userRepository.findByDocumentNumber(token, "11223344");

        StepVerifier.create(result)
                .expectNext(expectedUser)
                .verifyComplete();

        verify(userRepository, times(1)).findByDocumentNumber(token, "11223344");
    }

    @Test
    void testFindByDocumentNumberNotFound() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        when(userRepository.findByDocumentNumber(eq(token), eq("00000000")))
                .thenReturn(Mono.empty());

        Mono<User> result = userRepository.findByDocumentNumber(token, "00000000");

        StepVerifier.create(result)
                .verifyComplete();

        verify(userRepository, times(1)).findByDocumentNumber(token, "00000000");
    }

    @Test
    void testFindByIdFound() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        BigInteger userId = BigInteger.ONE;

        User expectedUser = User.builder()
                .id(userId)
                .name("Lucia")
                .lastName("Fernandez")
                .dateOfBirth(LocalDate.of(1988, 7, 15))
                .address("Av. Primavera 456")
                .email("lucia@example.com")
                .phone("999888777")
                .baseSalary(BigDecimal.valueOf(4200))
                .documentNumber("87654321")
                .build();

        when(userRepository.findById(eq(token), eq(userId)))
                .thenReturn(Mono.just(expectedUser));

        Mono<User> result = userRepository.findById(token, userId);

        StepVerifier.create(result)
                .expectNext(expectedUser)
                .verifyComplete();

        verify(userRepository, times(1)).findById(token, userId);
    }

    @Test
    void testFindByIdNotFound() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        BigInteger userId = BigInteger.TEN;

        when(userRepository.findById(eq(token), eq(userId)))
                .thenReturn(Mono.empty());

        Mono<User> result = userRepository.findById(token, userId);

        StepVerifier.create(result)
                .verifyComplete();

        verify(userRepository, times(1)).findById(token, userId);
    }
}

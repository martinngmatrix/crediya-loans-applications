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
    void testCreateUser() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        User user = User.builder()
                .name("Pedro")
                .lastName("Martínez")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Calle 123")
                .email("pedro@example.com")
                .phone("123456789")
                .baseSalary(BigDecimal.valueOf(4000))
                .documentNumber("12345678")
                .build();

        when(userRepository.createUser(any(User.class))).thenReturn(Mono.empty());

        Mono<Void> result = userRepository.createUser(user);

        StepVerifier.create(result)
                .verifyComplete();

        verify(userRepository, times(1)).createUser(user);
    }

        @Test
    void testFindByEmailFound() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        User expectedUser = User.builder()
                .name("Ana")
                .lastName("Lopez")
                .dateOfBirth(LocalDate.of(1985, 5, 15))
                .address("Av. Siempre Viva 742")
                .email("ana@example.com")
                .phone("987654321")
                .baseSalary(BigDecimal.valueOf(5000))
                .documentNumber("87654321")
                .build();

        when(userRepository.findByEmail(eq("ana@example.com")))
                .thenReturn(Mono.just(expectedUser));

        Mono<User> result = userRepository.findByEmail("ana@example.com");

        StepVerifier.create(result)
                .expectNext(expectedUser)
                .verifyComplete();

        verify(userRepository, times(1)).findByEmail("ana@example.com");
    }

    @Test
    void testFindByEmailNotFound() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        when(userRepository.findByEmail(eq("noexiste@example.com")))
                .thenReturn(Mono.empty());

        Mono<User> result = userRepository.findByEmail("noexiste@example.com");

        StepVerifier.create(result)
                .verifyComplete();

        verify(userRepository, times(1)).findByEmail("noexiste@example.com");
    }

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

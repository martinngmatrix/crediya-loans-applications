package co.com.bancolombia.usecase.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.utils.JwtService;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class JwtServiceTest {

    @Test
    void shouldUseJwtServiceMock() {
        JwtService jwtService = mock(JwtService.class);
        User user = new User();

        when(jwtService.generateToken(user)).thenReturn(Mono.just("mocked-token"));
        when(jwtService.validateToken("mocked-token")).thenReturn(Mono.just(true));
        when(jwtService.getRoleFromToken("mocked-token")).thenReturn(Mono.just("USER"));

        StepVerifier.create(jwtService.generateToken(user))
                .expectNext("mocked-token")
                .verifyComplete();

        StepVerifier.create(jwtService.validateToken("mocked-token"))
                .expectNext(true)
                .verifyComplete();

        StepVerifier.create(jwtService.getRoleFromToken("mocked-token"))
                .expectNext("USER")
                .verifyComplete();

        verify(jwtService).generateToken(user);
        verify(jwtService).validateToken("mocked-token");
        verify(jwtService).getRoleFromToken("mocked-token");
    }
}

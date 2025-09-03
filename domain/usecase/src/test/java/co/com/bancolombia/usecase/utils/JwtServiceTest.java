package co.com.bancolombia.usecase.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.utils.JwtService;

public class JwtServiceTest {

    @Test
    void shouldUseJwtServiceMock() {
        JwtService jwtService = mock(JwtService.class);
        User user = new User();

        when(jwtService.generateToken(user)).thenReturn("mocked-token");
        when(jwtService.validateToken("mocked-token")).thenReturn(true);
        when(jwtService.getRoleFromToken("mocked-token")).thenReturn("USER");

        assertEquals("mocked-token", jwtService.generateToken(user));
        assertTrue(jwtService.validateToken("mocked-token"));
        assertEquals("USER", jwtService.getRoleFromToken("mocked-token"));

        verify(jwtService).generateToken(user);
        verify(jwtService).validateToken("mocked-token");
        verify(jwtService).getRoleFromToken("mocked-token");
    }
}

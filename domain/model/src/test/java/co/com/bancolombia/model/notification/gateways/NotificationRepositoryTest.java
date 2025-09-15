package co.com.bancolombia.model.notification.gateways;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import co.com.bancolombia.model.notification.Notification;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class NotificationRepositoryTest {

    private NotificationRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(NotificationRepository.class);
    }

    @Test
    void shouldSendNotificationSuccessfully() {
        Notification notification = new Notification(
                Map.of("message", "Hola"),
                "queue-1"
        );

        when(repository.sendNotification(any(Notification.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(repository.sendNotification(notification))
                .verifyComplete();

        verify(repository, times(1)).sendNotification(notification);
    }

    @Test
    void shouldReturnErrorWhenNotificationFails() {
        Notification notification = new Notification(
                Map.of("message", "Error"),
                "queue-2"
        );

        when(repository.sendNotification(any(Notification.class)))
                .thenReturn(Mono.error(new RuntimeException("Fallo en envío")));

        StepVerifier.create(repository.sendNotification(notification))
                .expectErrorMatches(err -> err instanceof RuntimeException &&
                        err.getMessage().equals("Fallo en envío"))
                .verify();

        verify(repository, times(1)).sendNotification(notification);
    }
}
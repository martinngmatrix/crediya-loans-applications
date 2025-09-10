package co.com.bancolombia.model.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void shouldBuildNotificationWithBuilder() {
        Notification notification = Notification.builder()
                .message("Hola")
                .email("test@email.com")
                .build();

        assertEquals("Hola", notification.getMessage());
        assertEquals("test@email.com", notification.getEmail());
    }

    @Test
    void shouldUseSettersAndGetters() {
        Notification notification = new Notification(null, null);

        notification.setMessage("Nuevo mensaje");
        notification.setEmail("nuevo@email.com");

        assertEquals("Nuevo mensaje", notification.getMessage());
        assertEquals("nuevo@email.com", notification.getEmail());
    }

    @Test
    void shouldCreateNotificationWithAllArgsConstructor() {
        Notification notification = new Notification("Mensaje directo", "directo@email.com");

        assertEquals("Mensaje directo", notification.getMessage());
        assertEquals("directo@email.com", notification.getEmail());
    }

    @Test
    void shouldCopyNotificationWithToBuilder() {
        Notification notification = Notification.builder()
                .message("Original")
                .email("original@email.com")
                .build();

        Notification copy = notification.toBuilder()
                .message("Modificado")
                .build();

        assertEquals("Modificado", copy.getMessage());
        assertEquals("original@email.com", copy.getEmail());
    }
}
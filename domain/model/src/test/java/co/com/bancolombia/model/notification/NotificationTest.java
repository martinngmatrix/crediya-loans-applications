package co.com.bancolombia.model.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;

import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void shouldBuildNotificationWithBuilder() {
        Notification notification = Notification.builder()
                .payload(Map.of("message", "Hola mundo"))
                .queueKey("test-queue")
                .build();

        assertEquals("Hola mundo", notification.getPayload().get("message"));
        assertEquals("test-queue", notification.getQueueKey());
    }

    @Test
    void shouldUseSettersAndGetters() {
        Notification notification = new Notification(null, null);

        notification.setPayload(Map.of("key", "Nuevo payload"));
        notification.setQueueKey("queue-1");

        assertEquals("Nuevo payload", notification.getPayload().get("key"));
        assertEquals("queue-1", notification.getQueueKey());
    }

    @Test
    void shouldCreateNotificationWithAllArgsConstructor() {
        Notification notification = new Notification(
                Map.of("direct", "Payload directo"),
                "queue-2"
        );

        assertEquals("Payload directo", notification.getPayload().get("direct"));
        assertEquals("queue-2", notification.getQueueKey());
    }

    @Test
    void shouldCopyNotificationWithToBuilder() {
        Notification notification = Notification.builder()
                .payload(Map.of("key", "Original"))
                .queueKey("queue-3")
                .build();

        Notification copy = notification.toBuilder()
                .payload(Map.of("key", "Modificado"))
                .build();

        assertEquals("Modificado", copy.getPayload().get("key"));
        assertEquals("queue-3", copy.getQueueKey()); // se mantiene igual
    }

    @Test
    void shouldAllowNullPayloadAndQueueKey() {
        Notification notification = new Notification(null, null);

        assertNull(notification.getPayload());
        assertNull(notification.getQueueKey());
    }
}

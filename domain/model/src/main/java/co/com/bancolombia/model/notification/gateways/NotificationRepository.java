package co.com.bancolombia.model.notification.gateways;
import co.com.bancolombia.model.notification.Notification;
import reactor.core.publisher.Mono;

public interface NotificationRepository {
    Mono<Void> sendNotification(Notification notification);
}

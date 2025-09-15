package co.com.bancolombia.sqs.sender;

import co.com.bancolombia.model.notification.Notification;
import co.com.bancolombia.model.notification.gateways.NotificationRepository;
import co.com.bancolombia.sqs.sender.config.SQSSenderProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSSender implements NotificationRepository {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final ObjectMapper mapper;

    public Mono<String> send(String message) {
        return Mono.fromCallable(() -> buildRequest(message))
                .flatMap(request -> Mono.fromFuture(client.sendMessage(request)))
                .doOnNext(response -> log.debug("Message sent {}", response.messageId()))
                .map(SendMessageResponse::messageId);
    }

    private SendMessageRequest buildRequest(String message) {
        return SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(message)
                .build();
    }

    @Override
    public Mono<Void> sendNotification(Notification notification) {
        log.info("Message to send {}", notification.getPayload());
        return Mono.fromCallable(() -> {
            try {
                String json = mapper.writeValueAsString(notification);
                String queueUrl = properties.queues().get(notification.getQueueKey());
                log.info("QueueKey={}, QueueUrl={}", notification.getQueueKey(), queueUrl);
                return notification.getQueueKey() == null || queueUrl == null
                        ? buildRequest(json):
                        SendMessageRequest.builder()
                                .queueUrl(queueUrl)
                                .messageBody(json)
                                .build();
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error serializando notificación", e);
            }
        })
        .flatMap(request -> Mono.fromFuture(client.sendMessage(request))
        .doOnNext(response -> log.info("Notification sent {}", response)))
        .doOnError(error -> log.error("Error sending notification", error))
        .then();
    }
}

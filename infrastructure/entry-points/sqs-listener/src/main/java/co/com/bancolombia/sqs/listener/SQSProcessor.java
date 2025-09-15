package co.com.bancolombia.sqs.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.com.bancolombia.model.debtcapacity.DebtCapacity;
import co.com.bancolombia.sqs.listener.config.SQSProperties;
import co.com.bancolombia.sqs.listener.helper.SQSListener;
import co.com.bancolombia.usecase.loanapplication.LoanApplicationUseCase;
import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {
    private final LoanApplicationUseCase loanApplicationUseCase;
    private final SqsAsyncClient client;
    private final SQSProperties properties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        SQSListener.builder()
                .client(client)
                .properties(properties)
                .processor(this)
                .build()
                .start();
    }

    @Override
    public Mono<Void> apply(Message message) {
        log.info("Mensaje recibido {}", message.body());
        return Mono.fromCallable(() -> objectMapper.readValue(message.body(), DebtCapacity.class))
                .flatMap(loanApplicationUseCase::processDebtCapacityResult)
                .onErrorResume(ex -> {
                    log.error("Error procesando mensaje SQS: {}", ex.getMessage(), ex);
                    return Mono.empty();
                });
    }
}

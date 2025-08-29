package co.com.bancolombia.api;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.bancolombia.api.dto.CreateLoanApplicationDTO;
import co.com.bancolombia.api.dto.ErrorResponse;
import co.com.bancolombia.api.mapper.LoanApplicationDTOMapper;
import co.com.bancolombia.api.validation.ValidationService;
import co.com.bancolombia.usecase.loanapplication.LoanApplicationUseCase;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {
private final LoanApplicationUseCase loanApplicationUseCase;
private final LoanApplicationDTOMapper loanApplicationMapper;
private final ValidationService validationService;
private static final Logger log = LoggerFactory.getLogger(Handler.class);

    public Mono<ServerResponse> createLoanApplication(ServerRequest serverRequest) {
        log.trace("Inicio del proceso de creación de solicitud de préstamo");
        return serverRequest.bodyToMono(CreateLoanApplicationDTO.class)
                .flatMap(validationService::validate)
                .flatMap(dto -> {
                    var loanApplication = loanApplicationMapper.toModel(dto);
                    String documentNumber = dto.documentNumber();
                    String loanType = dto.loanType();

                    return loanApplicationUseCase.createLoanApplication(loanApplication, documentNumber, loanType);
                })
                .doOnSuccess(loanApp -> log.info("Solicitud de préstamo creada con exito"))
                .then(ServerResponse.ok().bodyValue(
                    Map.of("message", "Solicitud de préstamo creada con exito")
                ))
                .onErrorResume(e -> {
                    ErrorResponse error = new ErrorResponse(
                            e.getMessage() != null ? e.getMessage() : "Error inesperado",
                            400
                    );
                    return ServerResponse.status(400).bodyValue(error);
                });
    }
}

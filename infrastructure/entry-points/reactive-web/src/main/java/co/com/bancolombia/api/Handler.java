package co.com.bancolombia.api;

import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.bancolombia.api.constants.Constants;
import co.com.bancolombia.api.constants.messages.ApiResponseMessages;
import co.com.bancolombia.api.dto.CalculateDebtCapacityDTO;
import co.com.bancolombia.api.dto.CreateLoanApplicationDTO;
import co.com.bancolombia.api.dto.ErrorResponse;
import co.com.bancolombia.api.dto.UpdateLoanApplicationDTO;
import co.com.bancolombia.api.mapper.LoanApplicationDTOMapper;
import co.com.bancolombia.api.utils.JwtUtil;
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
        log.trace(ApiResponseMessages.REQUEST_RECEIVED_LOAN_APPLICATION);
        String token = JwtUtil.extractToken(serverRequest);
        return serverRequest.bodyToMono(CreateLoanApplicationDTO.class)
                .flatMap(validationService::validate)
                .flatMap(dto -> {
                    var loanApplication = loanApplicationMapper.toModel(dto);
                    String documentNumber = dto.documentNumber();
                    String loanType = dto.loanType();
                    Boolean automaticValidation = dto.automaticValidation();

                    return loanApplicationUseCase.createLoanApplication(token, loanApplication, documentNumber, loanType, automaticValidation);
                })
                .doOnSuccess(loanApp -> log.info(ApiResponseMessages.LOAN_APPLICATION_CREATED))
                .then(ServerResponse
                .status(HttpStatus.CREATED)
                .build())
                .onErrorResume(e -> {
                    ErrorResponse error = new ErrorResponse(
                            e.getMessage() != null ? e.getMessage() : Constants.UNEXPECTED_ERROR
                    );
                    return ServerResponse.status(400).bodyValue(error);
                });
    }

    public Mono<ServerResponse> listLoanApplication(ServerRequest serverRequest) {
        log.trace(ApiResponseMessages.REQUEST_RECEIVED_LIST_LOAN_APPLICATION);
        String token = JwtUtil.extractToken(serverRequest);
        int page = serverRequest.queryParam("page").map(Integer::parseInt).orElse(0);
        int size = serverRequest.queryParam("size").map(Integer::parseInt).orElse(0);
        String status = serverRequest.queryParam("status").orElse("");
        return loanApplicationUseCase.listLoanApplications(token, status, size, page)
                .flatMap(
                    list -> ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(list)
                )
                .doOnSuccess(res -> log.info(ApiResponseMessages.REQUEST_PROCESSED_SUCCESSFULLY))
                .onErrorResume(e -> {
                    ErrorResponse error = new ErrorResponse(
                            e.getMessage() != null ? e.getMessage() : Constants.UNEXPECTED_ERROR
                    );
                    return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(error);
                }); 
    }

    public Mono<ServerResponse> updateLoanApplicationStatus(ServerRequest serverRequest) {
        log.trace(ApiResponseMessages.REQUEST_RECEIVED_UPDATE_LOAN_APPLICATION_STATUS);
        String token = JwtUtil.extractToken(serverRequest);
        return serverRequest.bodyToMono(UpdateLoanApplicationDTO.class)
                .flatMap(validationService::validate)
                .flatMap(body -> {
                    BigInteger id = body.id();
                    String status = body.status();
                    return loanApplicationUseCase.updateLoanApplicationStatus(token, id, status);
                })
                .then(ServerResponse.status(HttpStatus.NO_CONTENT).build())
                .doOnSuccess(res -> log.info(ApiResponseMessages.REQUEST_PROCESSED_SUCCESSFULLY))
                .onErrorResume(e -> {
                    ErrorResponse error = new ErrorResponse(
                            e.getMessage() != null ? e.getMessage() : Constants.UNEXPECTED_ERROR
                    );
                    return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(error);
                });
    }

    public Mono<ServerResponse> calculateDebtCapacity(ServerRequest serverRequest) {
        log.trace(ApiResponseMessages.REQUEST_RECEIVED_CALCULATE_DEBT_CAPACITY);
        String token = JwtUtil.extractToken(serverRequest);
        return serverRequest.bodyToMono(CalculateDebtCapacityDTO.class)
                .flatMap(body ->
                    loanApplicationUseCase.calculateDebtCapacity(
                        token,
                        body.userId(), 
                        body.loanApplicationId()
                    )
                )
                .then(ServerResponse.status(HttpStatus.ACCEPTED).build())
                .doOnSuccess(res -> log.info(ApiResponseMessages.REQUEST_CALCULATE_DEBT_PROCESSED_SUCCESSFULLY))
                .onErrorResume(e -> {
                    ErrorResponse error = new ErrorResponse(
                            e.getMessage() != null ? e.getMessage() : Constants.UNEXPECTED_ERROR
                    );
                    return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(error);
                });
    }
}

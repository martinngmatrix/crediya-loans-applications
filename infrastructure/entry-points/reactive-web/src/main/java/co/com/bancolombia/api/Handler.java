package co.com.bancolombia.api;

import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.bancolombia.api.constants.Constants;
import co.com.bancolombia.api.constants.messages.ApiResponseMessages;
import co.com.bancolombia.api.dto.CreateLoanApplicationDTO;
import co.com.bancolombia.api.dto.ErrorResponse;
import co.com.bancolombia.api.dto.ListLoanApplicationDTO;
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

                    return loanApplicationUseCase.createLoanApplication(token, loanApplication, documentNumber, loanType);
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
                .map(loanAppDetails -> new ListLoanApplicationDTO(
                    loanAppDetails.getId(),
                    loanAppDetails.getEmail(),
                    loanAppDetails.getName(),
                    loanAppDetails.getLoanName(),
                    loanAppDetails.getInterestRate(),
                    loanAppDetails.getDeudaTotalMensualSolicitudesAprobadas(),
                    loanAppDetails.getBaseSalary(),
                    loanAppDetails.getAmount(),
                    loanAppDetails.getTerm(),
                    loanAppDetails.getStatus()
                ))
                .collectList()
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
}

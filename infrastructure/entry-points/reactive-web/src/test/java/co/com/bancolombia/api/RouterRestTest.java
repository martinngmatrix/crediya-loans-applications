package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CreateLoanApplicationDTO;
import co.com.bancolombia.api.mapper.LoanApplicationDTOMapper;
import co.com.bancolombia.api.validation.ValidationService;
import co.com.bancolombia.model.loanapplication.LoanApplication;
import co.com.bancolombia.usecase.loanapplication.LoanApplicationUseCase;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private LoanApplicationUseCase loanApplicationUseCase;

    @MockitoBean
    private LoanApplicationDTOMapper loanApplicationMapper;

    @MockitoBean
    private ValidationService validationService;

    @Test
    void testCreateLoanApplication() {
        CreateLoanApplicationDTO request = new CreateLoanApplicationDTO(
                "987654321",
                BigDecimal.valueOf(1500),
                12,
                "Personal"
        );

        LoanApplication loanApplication = LoanApplication.builder()
                .id(BigInteger.ONE)
                .userId(BigInteger.TEN)
                .amount(BigDecimal.valueOf(1500))
                .term(12)
                .loanId(BigInteger.valueOf(1))
                .build();

        when(validationService.validate(any(CreateLoanApplicationDTO.class))).thenReturn(Mono.just(request));
        when(loanApplicationMapper.toModel(any(CreateLoanApplicationDTO.class))).thenReturn(loanApplication);
        when(loanApplicationUseCase.createLoanApplication(any(LoanApplication.class), anyString(), anyString()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/api/v1/solicitud")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().isEmpty();
    }

    @Test
    void testCreateLoanApplicationValidationError() {
        CreateLoanApplicationDTO request = new CreateLoanApplicationDTO(
                "",
                BigDecimal.valueOf(1500),
                12,
                "Personal"
        );

        when(validationService.validate(any(CreateLoanApplicationDTO.class)))
                .thenReturn(Mono.error(new IllegalArgumentException("El número de documento no puede estar vacío")));

        webTestClient.post()
                .uri("/api/v1/solicitud")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("El número de documento no puede estar vacío");
    }
}

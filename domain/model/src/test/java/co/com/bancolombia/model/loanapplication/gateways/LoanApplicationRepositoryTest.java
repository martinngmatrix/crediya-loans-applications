package co.com.bancolombia.model.loanapplication.gateways;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import co.com.bancolombia.model.loanapplication.LoanApplication;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class LoanApplicationRepositoryTest {

    @Test
    void testCreateLoanApplication() {
        LoanApplicationRepository repository = Mockito.mock(LoanApplicationRepository.class);

        LoanApplication loanApplication = LoanApplication.builder()
                .id(BigInteger.ONE)
                .userId(BigInteger.valueOf(100))
                .loanId(BigInteger.valueOf(200))
                .amount(BigDecimal.valueOf(15000))
                .term(24)
                .status("PENDING")
                .build();

        String documentNumber = "987654321";
        String loanType = "Personal";

        when(repository.createLoanApplication(any(LoanApplication.class), any(String.class), any(String.class)))
                .thenReturn(Mono.empty());

        Mono<Void> result = repository.createLoanApplication(loanApplication, documentNumber, loanType);

        StepVerifier.create(result)
                .verifyComplete();

        verify(repository, times(1)).createLoanApplication(loanApplication, documentNumber, loanType);
    }
}

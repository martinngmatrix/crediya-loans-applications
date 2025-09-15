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
import co.com.bancolombia.model.loanapplication.constants.Constants;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class LoanApplicationRepositoryTest {

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

        Mono<LoanApplication> result = repository.createLoanApplication(loanApplication, documentNumber, loanType);

        StepVerifier.create(result)
                .verifyComplete();

        verify(repository, times(1)).createLoanApplication(loanApplication, documentNumber, loanType);
    }

    @Test
    void testGetLoansApplicationsWithResults() {
        LoanApplicationRepository repository = Mockito.mock(LoanApplicationRepository.class);

        LoanApplication app1 = LoanApplication.builder()
                .id(BigInteger.ONE)
                .userId(BigInteger.valueOf(100))
                .loanId(BigInteger.valueOf(200))
                .amount(BigDecimal.valueOf(10000))
                .term(12)
                .status("PENDING")
                .build();

        LoanApplication app2 = LoanApplication.builder()
                .id(BigInteger.TWO)
                .userId(BigInteger.valueOf(101))
                .loanId(BigInteger.valueOf(201))
                .amount(BigDecimal.valueOf(20000))
                .term(36)
                .status("APPROVED")
                .build();

        when(repository.getLoansApplicationsWithPagination(Constants.PENDING_REVIEW,1,1)).thenReturn(Flux.just(app1, app2));

        StepVerifier.create(repository.getLoansApplicationsWithPagination(Constants.PENDING_REVIEW,1,1))
                .expectNext(app1)
                .expectNext(app2)
                .verifyComplete();

        verify(repository, times(1)).getLoansApplicationsWithPagination(Constants.PENDING_REVIEW,1,1);
    }

    @Test
    void testUpdateLoanApplicationStatus() {
        LoanApplicationRepository repository = Mockito.mock(LoanApplicationRepository.class);

        BigInteger id = BigInteger.ONE;
        String newStatus = "APPROVED";

        LoanApplication updatedLoan = LoanApplication.builder()
                .id(id)
                .userId(BigInteger.valueOf(100))
                .loanId(BigInteger.valueOf(200))
                .amount(BigDecimal.valueOf(15000))
                .term(24)
                .status(newStatus)
                .build();

        when(repository.updateLoanApplicationStatus(id, newStatus)).thenReturn(Mono.just(updatedLoan));

        StepVerifier.create(repository.updateLoanApplicationStatus(id, newStatus))
                .expectNext(updatedLoan)
                .verifyComplete();

        verify(repository, times(1)).updateLoanApplicationStatus(id, newStatus);
    }

    @Test
    void testGetApprovedLoansApplications() {
        LoanApplicationRepository repository = Mockito.mock(LoanApplicationRepository.class);
        BigInteger userId = BigInteger.valueOf(100);
        
        LoanApplication approved1 = LoanApplication.builder()
                .id(BigInteger.ONE)
                .userId(userId)
                .loanId(BigInteger.valueOf(200))
                .amount(BigDecimal.valueOf(12000))
                .term(24)
                .status("APPROVED")
                .build();

        LoanApplication approved2 = LoanApplication.builder()
                .id(BigInteger.TWO)
                .userId(userId)
                .loanId(BigInteger.valueOf(201))
                .amount(BigDecimal.valueOf(18000))
                .term(36)
                .status("APPROVED")
                .build();

        when(repository.getApprovedLoansApplications(userId))
                .thenReturn(Flux.just(approved1, approved2));

        StepVerifier.create(repository.getApprovedLoansApplications(userId))
                .expectNext(approved1)
                .expectNext(approved2)
                .verifyComplete();

        verify(repository, times(1)).getApprovedLoansApplications(userId);
    }
}

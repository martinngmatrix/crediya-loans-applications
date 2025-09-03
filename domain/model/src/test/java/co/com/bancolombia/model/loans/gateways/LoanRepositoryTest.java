package co.com.bancolombia.model.loans.gateways;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import co.com.bancolombia.model.loans.Loan;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class LoanRepositoryTest {

    private LoanRepository loanRepository;

    @BeforeEach
    void setUp() {
        loanRepository = Mockito.mock(LoanRepository.class);
    }

    @Test
    void testFindByNameReturnsLoan() {
        Loan loan = Loan.builder()
                .id(BigInteger.ONE)
                .name("Personal Loan")
                .build();

        when(loanRepository.findByName("Personal Loan"))
                .thenReturn(Mono.just(loan));

        StepVerifier.create(loanRepository.findByName("Personal Loan"))
                .expectNextMatches(result ->
                        result.getId().equals(BigInteger.ONE)
                                && result.getName().equals("Personal Loan"))
                .verifyComplete();

        verify(loanRepository, times(1)).findByName("Personal Loan");
    }

    @Test
    void testFindByNameReturnsEmpty() {
        when(loanRepository.findByName("Unknown"))
                .thenReturn(Mono.empty());

        StepVerifier.create(loanRepository.findByName("Unknown"))
                .verifyComplete();

        verify(loanRepository, times(1)).findByName("Unknown");
    }

    @Test
    void testFindByIdReturnsLoan() {
        Loan loan = Loan.builder()
                .id(BigInteger.ONE)
                .name("Personal Loan")
                .build();

        when(loanRepository.findById(BigInteger.valueOf(1)))
                .thenReturn(Mono.just(loan));

        StepVerifier.create(loanRepository.findById(BigInteger.valueOf(1)))
                .expectNextMatches(result ->
                        result.getId().equals(BigInteger.ONE)
                                && result.getName().equals("Personal Loan"))
                .verifyComplete();

        verify(loanRepository, times(1)).findById(BigInteger.valueOf(1));
    }

    @Test
    void testFindByIdReturnsEmpty() {
        when(loanRepository.findById(BigInteger.valueOf(1)))
                .thenReturn(Mono.empty());

        StepVerifier.create(loanRepository.findById(BigInteger.valueOf(1)))
                .verifyComplete();

        verify(loanRepository, times(1)).findById(BigInteger.valueOf(1));
    }
}

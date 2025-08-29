package co.com.bancolombia.usecase.loanapplication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.com.bancolombia.model.loanapplication.LoanApplication;
import co.com.bancolombia.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loans.Loan;
import co.com.bancolombia.model.loans.gateways.LoanRepository;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class LoanApplicationUseCaseTest {

    private LoanApplicationRepository repository;
    private LoanRepository loanRepository;
    private UserRepository userRepository;
    private LoanApplicationUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(LoanApplicationRepository.class);
        loanRepository = mock(LoanRepository.class);
        userRepository = mock(UserRepository.class);
        useCase = new LoanApplicationUseCase(repository, loanRepository, userRepository);
    }

    @Test
    void createLoanApplicationSuccess() {
        LoanApplication loanApplication = new LoanApplication();
        String documentNumber = "123456789";
        String loanType = "Personal";

        User user = User.builder().id(BigInteger.ONE).documentNumber(documentNumber).build();
        Loan loan = Loan.builder().id(BigInteger.TWO).name(loanType).build();

        when(userRepository.findByDocumentNumber(documentNumber)).thenReturn(Mono.just(user));
        when(loanRepository.findByName(loanType)).thenReturn(Mono.just(loan));
        when(repository.createLoanApplication(any(), eq(documentNumber), eq(loanType))).thenReturn(Mono.empty());

        Mono<Void> result = useCase.createLoanApplication(loanApplication, documentNumber, loanType);

        StepVerifier.create(result).verifyComplete();

        verify(repository, times(1)).createLoanApplication(loanApplication, documentNumber, loanType);
        verify(userRepository, times(1)).findByDocumentNumber(documentNumber);
        verify(loanRepository, times(1)).findByName(loanType);
    }

    @Test
    void createLoanApplicationUserNotFound() {
        LoanApplication loanApplication = new LoanApplication();
        String documentNumber = "000000000";
        String loanType = "Personal";

        when(userRepository.findByDocumentNumber(documentNumber)).thenReturn(Mono.empty());

        Mono<Void> result = useCase.createLoanApplication(loanApplication, documentNumber, loanType);

        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof RuntimeException &&
                        err.getMessage().equals("Usuario no encontrado"))
                .verify();

        verify(userRepository, times(1)).findByDocumentNumber(documentNumber);
        verifyNoInteractions(loanRepository, repository);
    }

    @Test
    void createLoanApplicationLoanNotFound() {
        LoanApplication loanApplication = new LoanApplication();
        String documentNumber = "123456789";
        String loanType = "Inexistente";

        User user = User.builder().id(BigInteger.ONE).documentNumber(documentNumber).build();

        when(userRepository.findByDocumentNumber(documentNumber)).thenReturn(Mono.just(user));
        when(loanRepository.findByName(loanType)).thenReturn(Mono.empty());

        Mono<Void> result = useCase.createLoanApplication(loanApplication, documentNumber, loanType);

        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof RuntimeException &&
                        err.getMessage().equals("Tipo de préstamo no válido"))
                .verify();

        verify(userRepository, times(1)).findByDocumentNumber(documentNumber);
        verify(loanRepository, times(1)).findByName(loanType);
        verifyNoInteractions(repository);
    }

    @Test
    void createLoanApplicationRepositoryError() {
        LoanApplication loanApplication = new LoanApplication();
        String documentNumber = "123456789";
        String loanType = "Personal";

        User user = User.builder().id(BigInteger.ONE).documentNumber(documentNumber).build();
        Loan loan = Loan.builder().id(BigInteger.TWO).name(loanType).build();

        RuntimeException dbError = new RuntimeException("DB error");

        when(userRepository.findByDocumentNumber(documentNumber)).thenReturn(Mono.just(user));
        when(loanRepository.findByName(loanType)).thenReturn(Mono.just(loan));
        when(repository.createLoanApplication(any(), eq(documentNumber), eq(loanType)))
                .thenReturn(Mono.error(dbError));

        Mono<Void> result = useCase.createLoanApplication(loanApplication, documentNumber, loanType);

        StepVerifier.create(result)
                .expectErrorMatches(err -> err.getMessage().equals("DB error"))
                .verify();

        verify(repository, times(1)).createLoanApplication(loanApplication, documentNumber, loanType);
    }
}
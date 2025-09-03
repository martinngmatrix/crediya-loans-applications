package co.com.bancolombia.usecase.loanapplication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.com.bancolombia.model.loanapplication.LoanApplication;
import co.com.bancolombia.model.loanapplication.constants.Constants;
import co.com.bancolombia.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loans.Loan;
import co.com.bancolombia.model.loans.gateways.LoanRepository;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateways.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class LoanApplicationUseCaseTest {

    private LoanApplicationRepository repository;
    private LoanRepository loanRepository;
    private UserRepository userRepository;
    private LoanApplicationUseCase useCase;
    String token = "example_token";
    int size = 1;
    int page = 1;

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

        when(userRepository.findByDocumentNumber(token, documentNumber)).thenReturn(Mono.just(user));
        when(loanRepository.findByName(loanType)).thenReturn(Mono.just(loan));
        when(repository.createLoanApplication(any(), eq(documentNumber), eq(loanType))).thenReturn(Mono.empty());

        Mono<Void> result = useCase.createLoanApplication(token, loanApplication, documentNumber, loanType);

        StepVerifier.create(result).verifyComplete();

        verify(repository, times(1)).createLoanApplication(loanApplication, documentNumber, loanType);
        verify(userRepository, times(1)).findByDocumentNumber(token, documentNumber);
        verify(loanRepository, times(1)).findByName(loanType);
    }

    @Test
    void createLoanApplicationUserNotFound() {
        LoanApplication loanApplication = new LoanApplication();
        String documentNumber = "000000000";
        String loanType = "Personal";

        when(userRepository.findByDocumentNumber(token, documentNumber)).thenReturn(Mono.empty());

        Mono<Void> result = useCase.createLoanApplication(token, loanApplication, documentNumber, loanType);

        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof RuntimeException &&
                        err.getMessage().equals("Usuario no encontrado"))
                .verify();

        verify(userRepository, times(1)).findByDocumentNumber(token, documentNumber);
        verifyNoInteractions(loanRepository, repository);
    }

    @Test
    void createLoanApplicationLoanNotFound() {
        LoanApplication loanApplication = new LoanApplication();
        String documentNumber = "123456789";
        String loanType = "Inexistente";

        User user = User.builder().id(BigInteger.ONE).documentNumber(documentNumber).build();

        when(userRepository.findByDocumentNumber(token, documentNumber)).thenReturn(Mono.just(user));
        when(loanRepository.findByName(loanType)).thenReturn(Mono.empty());

        Mono<Void> result = useCase.createLoanApplication(token, loanApplication, documentNumber, loanType);

        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof RuntimeException &&
                        err.getMessage().equals("Tipo de préstamo no válido"))
                .verify();

        verify(userRepository, times(1)).findByDocumentNumber(token, documentNumber);
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

        when(userRepository.findByDocumentNumber(token, documentNumber)).thenReturn(Mono.just(user));
        when(loanRepository.findByName(loanType)).thenReturn(Mono.just(loan));
        when(repository.createLoanApplication(any(), eq(documentNumber), eq(loanType)))
                .thenReturn(Mono.error(dbError));

        Mono<Void> result = useCase.createLoanApplication(token, loanApplication, documentNumber, loanType);

        StepVerifier.create(result)
                .expectErrorMatches(err -> err.getMessage().equals("DB error"))
                .verify();

        verify(repository, times(1)).createLoanApplication(loanApplication, documentNumber, loanType);
    }

    @Test
    void listLoanApplicationsSuccess() {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setId(BigInteger.ONE);
        loanApplication.setUserId(BigInteger.TEN);
        loanApplication.setLoanId(BigInteger.valueOf(20));
        loanApplication.setAmount(BigDecimal.valueOf(10000.0));
        loanApplication.setTerm(12);
        loanApplication.setStatus(Constants.PENDING_REVIEW);

        User user = User.builder()
                .id(BigInteger.TEN)
                .email("test@email.com")
                .name("John Doe")
                .baseSalary(BigDecimal.valueOf(2000.0))
                .build();

        Loan loan = Loan.builder()
                .id(BigInteger.valueOf(20))
                .name(Constants.LOAN_TYPES_TO_REVIEW.get(0))
                .build();

        when(repository.getLoansApplicationsWithPagination(Constants.PENDING_REVIEW, size, page)).thenReturn(Flux.just(loanApplication));
        when(userRepository.findById(token, BigInteger.TEN)).thenReturn(Mono.just(user));
        when(loanRepository.findById(BigInteger.valueOf(20))).thenReturn(Mono.just(loan));

        StepVerifier.create(useCase.listLoanApplications(token, Constants.PENDING_REVIEW, size, page))
                .expectNextMatches(details ->
                        details.getId().equals(BigInteger.ONE) &&
                        details.getName().equals("John Doe") &&
                        details.getLoanName().equals(loan.getName())
                )
                .verifyComplete();

        verify(repository, times(1)).getLoansApplicationsWithPagination(Constants.PENDING_REVIEW, size, page);
        verify(userRepository, times(1)).findById(token, BigInteger.TEN);
        verify(loanRepository, times(1)).findById(BigInteger.valueOf(20));
    }

    @Test
    void listLoanApplicationsFilteredOut() {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setId(BigInteger.ONE);
        loanApplication.setUserId(BigInteger.TEN);
        loanApplication.setLoanId(BigInteger.valueOf(20));
        loanApplication.setAmount(BigDecimal.valueOf(10000.0));
        loanApplication.setTerm(12);
        loanApplication.setStatus("TipoNoRevisable");

        User user = User.builder()
                .id(BigInteger.TEN)
                .email("test@email.com")
                .name("John Doe")
                .baseSalary(BigDecimal.valueOf(2000.0))
                .build();

        Loan loan = Loan.builder()
                .id(BigInteger.valueOf(20))
                .name("TipoNoRevisable")
                .build();

        when(repository.getLoansApplicationsWithPagination(Constants.PENDING_REVIEW, size, page)).thenReturn(Flux.empty());
        when(userRepository.findById(token, BigInteger.TEN)).thenReturn(Mono.just(user));
        when(loanRepository.findById(BigInteger.valueOf(20))).thenReturn(Mono.just(loan));

        StepVerifier.create(useCase.listLoanApplications(token, Constants.PENDING_REVIEW, size, page))
                .verifyComplete();
    }

    @Test
    void listLoanApplicationsRepositoryError() {
        when(repository.getLoansApplicationsWithPagination(Constants.PENDING_REVIEW, size, page)).thenReturn(Flux.error(new RuntimeException("DB error")));

        StepVerifier.create(useCase.listLoanApplications(token, Constants.PENDING_REVIEW, size, page))
                .expectErrorMatches(err -> err.getMessage().equals("DB error"))
                .verify();

        verify(repository, times(1)).getLoansApplicationsWithPagination(Constants.PENDING_REVIEW, size, page);
    }
}
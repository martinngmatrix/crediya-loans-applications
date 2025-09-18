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

import co.com.bancolombia.model.debtcapacity.DebtCapacity;
import co.com.bancolombia.model.loanapplication.LoanApplication;
import co.com.bancolombia.model.loanapplication.constants.Constants;
import co.com.bancolombia.model.loanapplication.constants.messages.LoanApplicationErrorMessages;
import co.com.bancolombia.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loans.Loan;
import co.com.bancolombia.model.loans.gateways.LoanRepository;
import co.com.bancolombia.model.notification.gateways.NotificationRepository;
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
        private NotificationRepository notificationRepository;
        String token = "example_token";
        int size = 1;
        int page = 1;

        @BeforeEach
        void setUp() {
                repository = mock(LoanApplicationRepository.class);
                loanRepository = mock(LoanRepository.class);
                userRepository = mock(UserRepository.class);
                notificationRepository = mock(NotificationRepository.class);
                useCase = new LoanApplicationUseCase(repository, loanRepository, userRepository,
                                notificationRepository);
        }

        @Test
        void createLoanApplicationSuccess() {
                LoanApplication loanApplication = new LoanApplication();
                String documentNumber = "123456789";
                String loanType = "Personal";
                Boolean automaticValidation = false;

                User user = User.builder().id(BigInteger.ONE).documentNumber(documentNumber).build();
                Loan loan = Loan.builder().id(BigInteger.TWO).name(loanType).build();

                when(userRepository.findByDocumentNumber(token, documentNumber)).thenReturn(Mono.just(user));
                when(loanRepository.findByName(loanType)).thenReturn(Mono.just(loan));
                when(repository.createLoanApplication(any(), eq(documentNumber), eq(loanType)))
                                .thenReturn(Mono.empty());

                Mono<Void> result = useCase.createLoanApplication(token, loanApplication, documentNumber, loanType, automaticValidation);

                StepVerifier.create(result).verifyComplete();

                verify(repository, times(1)).createLoanApplication(loanApplication, documentNumber, loanType);
                verify(userRepository, times(1)).findByDocumentNumber(token, documentNumber);
                verify(loanRepository, times(1)).findByName(loanType);
        }

        @Test
        void createLoanApplicationWithAutomaticValidation() {
                LoanApplication loanApplication = new LoanApplication();
                loanApplication.setId(BigInteger.TEN);
                String documentNumber = "123456789";
                String loanType = "Personal";
                Boolean automaticValidation = true;

                User user = User.builder().id(BigInteger.ONE).documentNumber(documentNumber).email("user@test.com").baseSalary(BigDecimal.valueOf(3000)).build();
                Loan loan = Loan.builder().id(BigInteger.TWO).name(loanType).build();

                when(userRepository.findByDocumentNumber(token, documentNumber)).thenReturn(Mono.just(user));
                when(loanRepository.findByName(loanType)).thenReturn(Mono.just(loan));
                when(repository.createLoanApplication(any(), eq(documentNumber), eq(loanType)))
                        .thenReturn(Mono.just(loanApplication));

                when(userRepository.findById(token, user.getId())).thenReturn(Mono.just(user));
                when(repository.getApprovedLoansApplications(user.getId())).thenReturn(Flux.empty());
                when(repository.getLoanApplicationById(loanApplication.getId())).thenReturn(Mono.just(loanApplication));
                when(notificationRepository.sendNotification(any())).thenReturn(Mono.empty());

                StepVerifier.create(useCase.createLoanApplication(token, loanApplication, documentNumber, loanType, automaticValidation))
                        .verifyComplete();

                verify(repository).createLoanApplication(loanApplication, documentNumber, loanType);
                verify(notificationRepository).sendNotification(any());
        }

        @Test
        void createLoanApplicationUserNotFound() {
                LoanApplication loanApplication = new LoanApplication();
                String documentNumber = "000000000";
                String loanType = "Personal";

                when(userRepository.findByDocumentNumber(token, documentNumber)).thenReturn(Mono.empty());

                Mono<Void> result = useCase.createLoanApplication(token, loanApplication, documentNumber, loanType, false);

                StepVerifier.create(result)
                        .expectErrorMatches(err -> err instanceof RuntimeException &&
                                err.getMessage().equals(LoanApplicationErrorMessages.USER_NOT_FOUND))
                        .verify();

                verifyNoInteractions(loanRepository);
                verifyNoInteractions(repository);
        }

        @Test
        void createLoanApplicationLoanNotFound() {
                LoanApplication loanApplication = new LoanApplication();
                String documentNumber = "123456789";
                String loanType = "Inexistente";

                User user = User.builder().id(BigInteger.ONE).documentNumber(documentNumber).build();

                when(userRepository.findByDocumentNumber(token, documentNumber)).thenReturn(Mono.just(user));
                when(loanRepository.findByName(loanType)).thenReturn(Mono.empty());

                Mono<Void> result = useCase.createLoanApplication(token, loanApplication, documentNumber, loanType, false);

                StepVerifier.create(result)
                        .expectErrorMatches(err -> err instanceof RuntimeException &&
                                err.getMessage().equals(LoanApplicationErrorMessages.LOAN_TYPE_NOT_VALID))
                        .verify();
        }

        @Test
        void createLoanApplicationRepositoryError() {
        LoanApplication loanApplication = new LoanApplication();
        String documentNumber = "123456789";
        String loanType = "Personal";
        Boolean automaticValidation = false;

        User user = User.builder().id(BigInteger.ONE).documentNumber(documentNumber).build();
        Loan loan = Loan.builder().id(BigInteger.TWO).name(loanType).build();

        RuntimeException dbError = new RuntimeException("DB error");

        when(userRepository.findByDocumentNumber(token, documentNumber)).thenReturn(Mono.just(user));
        when(loanRepository.findByName(loanType)).thenReturn(Mono.just(loan));
        when(repository.createLoanApplication(any(), eq(documentNumber), eq(loanType)))
                .thenReturn(Mono.error(dbError));

        Mono<Void> result = useCase.createLoanApplication(token, loanApplication, documentNumber, loanType, automaticValidation);

        StepVerifier.create(result)
                .expectErrorMatches(err -> err instanceof RuntimeException &&
                        err.getMessage().equals("DB error"))
                .verify();

        verify(repository, times(1)).createLoanApplication(loanApplication, documentNumber, loanType);
        verify(userRepository, times(1)).findByDocumentNumber(token, documentNumber);
        verify(loanRepository, times(1)).findByName(loanType);
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

                when(repository.getLoansApplicationsWithPagination(Constants.PENDING_REVIEW, size, page))
                                .thenReturn(Flux.just(loanApplication));
                when(userRepository.findById(token, BigInteger.TEN)).thenReturn(Mono.just(user));
                when(loanRepository.findById(BigInteger.valueOf(20))).thenReturn(Mono.just(loan));

                StepVerifier.create(useCase.listLoanApplications(token, Constants.PENDING_REVIEW, size, page))
                                .expectNextMatches(details -> details.getContent().size() == 1 &&
                                                details.getContent().get(0).getId().equals(BigInteger.ONE) &&
                                                details.getContent().get(0).getName().equals("John Doe") &&
                                                details.getContent().get(0).getLoanName().equals(loan.getName()) &&
                                                details.getPage() == page &&
                                                details.getSize() == 1 &&
                                                details.isHasNext())
                                .verifyComplete();

                verify(repository, times(1)).getLoansApplicationsWithPagination(Constants.PENDING_REVIEW, size, page);
                verify(userRepository, times(1)).findById(token, BigInteger.TEN);
                verify(loanRepository, times(1)).findById(BigInteger.valueOf(20));
        }

        @Test
        void listLoanApplicationsFilteredOut() {
                StepVerifier.create(useCase.listLoanApplications(token, "TipoNoRevisable", size, page))
                                .expectErrorMatches(err -> err instanceof RuntimeException &&
                                                err.getMessage().equals(LoanApplicationErrorMessages.INVALID_STATUS))
                                .verify();

                verifyNoInteractions(repository);
        }

        @Test
        void listLoanApplicationsRepositoryError() {
                when(repository.getLoansApplicationsWithPagination(Constants.PENDING_REVIEW, size, page))
                                .thenReturn(Flux.error(new RuntimeException("DB error")));

                StepVerifier.create(useCase.listLoanApplications(token, Constants.PENDING_REVIEW, size, page))
                                .expectErrorMatches(err -> err.getMessage().equals("DB error"))
                                .verify();

                verify(repository, times(1)).getLoansApplicationsWithPagination(Constants.PENDING_REVIEW, size, page);
        }

        @Test
        void updateLoanApplicationStatusSuccess() {
                BigInteger id = BigInteger.ONE;
                String status = Constants.LOAN_TYPES_TO_UPDATE.get(0);

                LoanApplication updatedApp = LoanApplication.builder()
                                .id(id)
                                .userId(BigInteger.TEN)
                                .status(status)
                                .build();

                User user = User.builder()
                                .id(BigInteger.TEN)
                                .email("user@email.com")
                                .build();

                when(repository.updateLoanApplicationStatus(id, status)).thenReturn(Mono.just(updatedApp));
                when(userRepository.findById(token, updatedApp.getUserId())).thenReturn(Mono.just(user));
                when(notificationRepository.sendNotification(any())).thenReturn(Mono.empty());

                StepVerifier.create(useCase.updateLoanApplicationStatus(token, id, status))
                                .verifyComplete();

                verify(repository, times(1)).updateLoanApplicationStatus(id, status);
                verify(userRepository, times(1)).findById(token, updatedApp.getUserId());
                verify(notificationRepository, times(1)).sendNotification(any());
        }

        @Test
        void updateLoanApplicationStatusInvalidStatus() {
                BigInteger id = BigInteger.ONE;
                String status = "INVALID_STATUS";

                StepVerifier.create(useCase.updateLoanApplicationStatus(token, id, status))
                                .expectErrorMatches(err -> err instanceof RuntimeException &&
                                                err.getMessage().equals(LoanApplicationErrorMessages.INVALID_STATUS))
                                .verify();

                verifyNoInteractions(repository);
                verifyNoInteractions(userRepository);
                verifyNoInteractions(notificationRepository);
        }

        @Test
        void updateLoanApplicationStatusNotFound() {
                BigInteger id = BigInteger.ONE;
                String status = Constants.LOAN_TYPES_TO_UPDATE.get(0);

                when(repository.updateLoanApplicationStatus(id, status)).thenReturn(Mono.empty());

                StepVerifier.create(useCase.updateLoanApplicationStatus(token, id, status))
                                .expectErrorMatches(err -> err instanceof RuntimeException &&
                                                err.getMessage().equals(
                                                                LoanApplicationErrorMessages.LOAN_APPLICATION_NOT_FOUND))
                                .verify();

                verify(repository, times(1)).updateLoanApplicationStatus(id, status);
                verifyNoInteractions(userRepository);
                verifyNoInteractions(notificationRepository);
        }

        @Test
        void updateLoanApplicationStatusNotificationError() {
                BigInteger id = BigInteger.ONE;
                String status = Constants.LOAN_TYPES_TO_UPDATE.get(0);

                LoanApplication updatedApp = LoanApplication.builder()
                                .id(id)
                                .userId(BigInteger.TEN)
                                .status(status)
                                .build();

                User user = User.builder()
                                .id(BigInteger.TEN)
                                .email("user@email.com")
                                .build();

                when(repository.updateLoanApplicationStatus(id, status)).thenReturn(Mono.just(updatedApp));
                when(userRepository.findById(token, updatedApp.getUserId())).thenReturn(Mono.just(user));
                when(notificationRepository.sendNotification(any()))
                                .thenReturn(Mono.error(new RuntimeException("Notification error")));

                StepVerifier.create(useCase.updateLoanApplicationStatus(token, id, status))
                                .expectErrorMatches(err -> err.getMessage().equals("Notification error"))
                                .verify();

                verify(repository, times(1)).updateLoanApplicationStatus(id, status);
                verify(userRepository, times(1)).findById(token, updatedApp.getUserId());
                verify(notificationRepository, times(1)).sendNotification(any());
        }

        @Test
        void calculateDebtCapacitySuccess() {
                BigInteger userId = BigInteger.ONE;
                BigInteger applicationId = BigInteger.TEN;

                User user = User.builder()
                        .id(userId)
                        .email("user@test.com")
                        .baseSalary(BigDecimal.valueOf(3000))
                        .build();

                LoanApplication approvedLoan = LoanApplication.builder()
                        .id(BigInteger.valueOf(11))
                        .userId(userId)
                        .amount(BigDecimal.valueOf(2000))
                        .status("APPROVED")
                        .build();

                LoanApplication newLoan = LoanApplication.builder()
                        .id(applicationId)
                        .userId(userId)
                        .amount(BigDecimal.valueOf(5000))
                        .status(Constants.PENDING_REVIEW)
                        .build();

                when(userRepository.findById(token, userId)).thenReturn(Mono.just(user));
                when(repository.getApprovedLoansApplications(userId)).thenReturn(Flux.just(approvedLoan));
                when(repository.getLoanApplicationById(applicationId)).thenReturn(Mono.just(newLoan));
                when(notificationRepository.sendNotification(any())).thenReturn(Mono.empty());

                Mono<Void> result = useCase.calculateDebtCapacity(token, userId, applicationId);

                StepVerifier.create(result).verifyComplete();

                verify(userRepository).findById(token, userId);
                verify(repository).getApprovedLoansApplications(userId);
                verify(repository).getLoanApplicationById(applicationId);
                verify(notificationRepository).sendNotification(any());
        }

        @Test
        void processDebtCapacityResultSuccess() {
        DebtCapacity debtCapacity = DebtCapacity.builder()
                .loanApplicationId(BigInteger.ONE)
                .email("user@test.com")
                .result("APPROVED")
                .build();

        LoanApplication updatedApp = LoanApplication.builder()
                .id(debtCapacity.getLoanApplicationId())
                .status(debtCapacity.getResult())
                .build();

        when(repository.updateLoanApplicationStatus(debtCapacity.getLoanApplicationId(), debtCapacity.getResult()))
                .thenReturn(Mono.just(updatedApp));
        when(notificationRepository.sendNotification(any())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.processDebtCapacityResult(debtCapacity))
                .verifyComplete();

        verify(repository).updateLoanApplicationStatus(debtCapacity.getLoanApplicationId(), debtCapacity.getResult());
        verify(notificationRepository).sendNotification(any());
        }

        @Test
        void processDebtCapacityResultLoanApplicationNotFound() {
        DebtCapacity debtCapacity = DebtCapacity.builder()
                .loanApplicationId(BigInteger.ONE)
                .email("user@test.com")
                .result("APPROVED")
                .build();

        when(repository.updateLoanApplicationStatus(debtCapacity.getLoanApplicationId(), debtCapacity.getResult()))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.processDebtCapacityResult(debtCapacity))
                .expectErrorMatches(err -> err instanceof RuntimeException &&
                        err.getMessage().equals(LoanApplicationErrorMessages.LOAN_APPLICATION_NOT_FOUND))
                .verify();

        verify(repository).updateLoanApplicationStatus(debtCapacity.getLoanApplicationId(), debtCapacity.getResult());
        verifyNoInteractions(notificationRepository);
        }

        @Test
        void processDebtCapacityResultNotificationError() {
        DebtCapacity debtCapacity = DebtCapacity.builder()
                .loanApplicationId(BigInteger.ONE)
                .email("user@test.com")
                .result("APPROVED")
                .build();

        LoanApplication updatedApp = LoanApplication.builder()
                .id(debtCapacity.getLoanApplicationId())
                .status(debtCapacity.getResult())
                .build();

        when(repository.updateLoanApplicationStatus(debtCapacity.getLoanApplicationId(), debtCapacity.getResult()))
                .thenReturn(Mono.just(updatedApp));
        when(notificationRepository.sendNotification(any()))
                .thenReturn(Mono.error(new RuntimeException("Notification failed")));

        StepVerifier.create(useCase.processDebtCapacityResult(debtCapacity))
                .expectErrorMatches(err -> err.getMessage().equals("Notification failed"))
                .verify();

        verify(repository).updateLoanApplicationStatus(debtCapacity.getLoanApplicationId(), debtCapacity.getResult());
        verify(notificationRepository).sendNotification(any());
        }

        @Test
        void processDebtCapacityResultSuccessWithApprovedLoans() {
                DebtCapacity debtCapacity = DebtCapacity.builder()
                        .loanApplicationId(BigInteger.ONE)
                        .email("user@test.com")
                        .result(Constants.LOAN_TYPES_TO_UPDATE.get(0))
                        .build();

                LoanApplication updatedApp = LoanApplication.builder()
                        .id(debtCapacity.getLoanApplicationId())
                        .amount(BigDecimal.valueOf(5000))
                        .status(debtCapacity.getResult())
                        .build();

                when(repository.updateLoanApplicationStatus(debtCapacity.getLoanApplicationId(), debtCapacity.getResult()))
                        .thenReturn(Mono.just(updatedApp));
                when(repository.getLoanApplicationById(debtCapacity.getLoanApplicationId()))
                        .thenReturn(Mono.just(updatedApp));
                when(notificationRepository.sendNotification(any())).thenReturn(Mono.empty());

                StepVerifier.create(useCase.processDebtCapacityResult(debtCapacity))
                        .verifyComplete();

                verify(repository).updateLoanApplicationStatus(debtCapacity.getLoanApplicationId(), debtCapacity.getResult());
                verify(repository).getLoanApplicationById(debtCapacity.getLoanApplicationId());
                verify(notificationRepository, times(2)).sendNotification(any());
        }
}
package co.com.bancolombia.usecase.loanapplication;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import co.com.bancolombia.model.debtcapacity.DebtCapacity;
import co.com.bancolombia.model.loanapplication.LoanApplication;
import co.com.bancolombia.model.loanapplication.constants.Constants;
import co.com.bancolombia.model.loanapplication.constants.messages.LoanApplicationErrorMessages;
import co.com.bancolombia.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loanapplicationdetails.LoanApplicationDetails;
import co.com.bancolombia.model.loans.gateways.LoanRepository;
import co.com.bancolombia.model.notification.Notification;
import co.com.bancolombia.model.notification.gateways.NotificationRepository;
import co.com.bancolombia.model.user.gateways.UserRepository;
import co.com.bancolombia.utils.HtmlUtil;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
public class LoanApplicationUseCase {
    private final LoanApplicationRepository repository;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public Mono<Void> createLoanApplication(String token, LoanApplication loanApplication, String documentNumber, String loanType, Boolean automaticValidation) {
        return userRepository.findByDocumentNumber(token, documentNumber)
                .switchIfEmpty(Mono.error(new RuntimeException(LoanApplicationErrorMessages.USER_NOT_FOUND)))
                .flatMap(user -> loanRepository.findByName(loanType)
                        .switchIfEmpty(Mono.error(new RuntimeException(LoanApplicationErrorMessages.LOAN_TYPE_NOT_VALID)))
                        .flatMap(loan -> {
                            loanApplication.setUserId(user.getId());
                            loanApplication.setLoanId(loan.getId());
                            loanApplication.setStatus(Constants.PENDING_REVIEW);
                            return repository.createLoanApplication(loanApplication, documentNumber, loanType);
                        })
                )
                .flatMap(app -> {
                    if (automaticValidation) {
                        return calculateDebtCapacity(token, app.getUserId(), app.getId()).then();
                    }
                    return Mono.empty();
                });
    }

    public Mono<LoanApplicationDetails> listLoanApplications(String token, String status, int size, int page) {
        if(!Constants.LOAN_TYPES_TO_REVIEW.contains(status))
            return Mono.error(new RuntimeException(LoanApplicationErrorMessages.INVALID_STATUS));
        return repository.getLoansApplicationsWithPagination(status, size, page)
                .flatMap(
                    loanApp -> Mono.zip(
                        userRepository.findById(token, loanApp.getUserId()),
                        loanRepository.findById(loanApp.getLoanId()),
                        (user, loan) ->  new LoanApplicationDetails.Item(
                            loanApp.getId(),
                            user.getEmail(),
                            user.getName(),
                            loan.getName(),
                            loanApp.getInterestRate(),
                            BigDecimal.valueOf(0),
                            user.getBaseSalary(),
                            loanApp.getAmount(),
                            loanApp.getTerm(),
                            loanApp.getStatus()
                        )
                    )
                )
                .collectList()
                .map(list -> LoanApplicationDetails.builder()
                        .page(page)
                        .size(list.size())
                        .hasNext(list.size() == size)
                        .content(list)
                        .build()
                );
    }

    public Mono<Void> updateLoanApplicationStatus(String token, BigInteger id, String status) {
        if (!Constants.LOAN_TYPES_TO_UPDATE.contains(status)) {
            return Mono.error(new RuntimeException(LoanApplicationErrorMessages.INVALID_STATUS));
        }
        String notificationMessage = HtmlUtil.generateStatusHtml(id, status);
        return repository.updateLoanApplicationStatus(id, status)
                .switchIfEmpty(Mono.error(new RuntimeException(LoanApplicationErrorMessages.LOAN_APPLICATION_NOT_FOUND)))
                .flatMap(updatedApp ->
                    userRepository.findById(token, updatedApp.getUserId())
                        .flatMap(user -> {
                            Map<String, Object> payload = Map.of(
                                "email", user.getEmail(),
                                "message", notificationMessage
                            );
                                
                            return notificationRepository.sendNotification(
                                Notification.builder()
                                    .payload(
                                        payload
                                    )
                                    .queueKey("notifications")
                                    .build()
                            );
                            }
                        )
                )
                .then();
    }

    public Mono<Void> calculateDebtCapacity(String token, BigInteger userId, BigInteger applicationId) {
        return Mono.zip(
            userRepository.findById(token, userId),
            repository.getApprovedLoansApplications(userId).collectList(),
            repository.getLoanApplicationById(applicationId)
            )
            .flatMap(tuple -> {
                var user = tuple.getT1();
                var approvedLoans = tuple.getT2();
                var newLoan = tuple.getT3();
                Map<String, Object> payload = Map.of(
                    "loanApplicationId", applicationId,
                    "email", user.getEmail(),
                    "totalIncome", user.getBaseSalary(),
                    "approvedLoans", approvedLoans,
                    "newLoan", newLoan
                );

                return notificationRepository.sendNotification(
                            Notification.builder()
                                .payload(payload)
                                .build());
            });
    }

    public Mono<Void> processDebtCapacityResult(DebtCapacity debtCapacity) {
        String htmlPlan = HtmlUtil.generatePaymentPlanHtml(debtCapacity);

        return repository.updateLoanApplicationStatus(debtCapacity.getLoanApplicationId(), debtCapacity.getResult())
                .switchIfEmpty(Mono.error(new RuntimeException(LoanApplicationErrorMessages.LOAN_APPLICATION_NOT_FOUND)))
                .flatMap(updatedApp -> {
                    Mono<Void> notificationMono = notificationRepository.sendNotification(Notification.builder()
                            .payload(Map.of(
                                    "email", debtCapacity.getEmail(),
                                    "message", htmlPlan
                            ))
                            .queueKey("notifications")
                            .build());

                    Mono<Void> approvedLoansMono = Mono.empty();
                    if (debtCapacity.getResult().equals(Constants.LOAN_TYPES_TO_UPDATE.get(0))) {
                        approvedLoansMono = repository.getLoanApplicationById(debtCapacity.getLoanApplicationId())
                                .switchIfEmpty(Mono.error(new RuntimeException(LoanApplicationErrorMessages.LOAN_APPLICATION_NOT_FOUND)))
                                .flatMap(loanApp -> notificationRepository.sendNotification(Notification.builder()
                                        .payload(Map.of(
                                                "loanApplicationId", debtCapacity.getLoanApplicationId(),
                                                "status", debtCapacity.getResult(),
                                                "amount", loanApp.getAmount()
                                        ))
                                        .queueKey("approvedLoans")
                                        .build()));
                    }
                    return Mono.when(notificationMono, approvedLoansMono);
                });
    }
}

package co.com.bancolombia.usecase.loanapplication;

import java.math.BigDecimal;
import java.math.BigInteger;

import co.com.bancolombia.model.loanapplication.LoanApplication;
import co.com.bancolombia.model.loanapplication.constants.Constants;
import co.com.bancolombia.model.loanapplication.constants.messages.LoanApplicationErrorMessages;
import co.com.bancolombia.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loanapplicationdetails.LoanApplicationDetails;
import co.com.bancolombia.model.loans.gateways.LoanRepository;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
public class LoanApplicationUseCase {
    private final LoanApplicationRepository repository;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    public Mono<Void> createLoanApplication(String token, LoanApplication loanApplication, String documentNumber, String loanType) {
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
                );
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
}

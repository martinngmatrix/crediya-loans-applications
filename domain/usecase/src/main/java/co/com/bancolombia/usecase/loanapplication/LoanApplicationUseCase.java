package co.com.bancolombia.usecase.loanapplication;

import co.com.bancolombia.model.loanapplication.LoanApplication;
import co.com.bancolombia.model.loanapplication.constants.Constants;
import co.com.bancolombia.model.loanapplication.constants.messages.LoanApplicationErrorMessages;
import co.com.bancolombia.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.bancolombia.model.loans.gateways.LoanRepository;
import co.com.bancolombia.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
public class LoanApplicationUseCase {
    private final LoanApplicationRepository repository;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    public Mono<Void> createLoanApplication(LoanApplication loanApplication, String documentNumber, String loanType) {
        return userRepository.findByDocumentNumber(documentNumber)
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
}

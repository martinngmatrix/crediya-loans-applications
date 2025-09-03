package co.com.bancolombia.model.loanapplication.gateways;


import co.com.bancolombia.model.loanapplication.LoanApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanApplicationRepository {
    Mono<Void> createLoanApplication(LoanApplication loanApplication, String documentNumber, String loanType);
    Flux<LoanApplication> getLoansApplicationsWithPagination(String status, int size, int page);
}

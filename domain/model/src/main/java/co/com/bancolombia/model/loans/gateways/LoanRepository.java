package co.com.bancolombia.model.loans.gateways;

import co.com.bancolombia.model.loans.Loan;
import reactor.core.publisher.Mono;

public interface LoanRepository {
    Mono<Loan> findByName(String name);
}

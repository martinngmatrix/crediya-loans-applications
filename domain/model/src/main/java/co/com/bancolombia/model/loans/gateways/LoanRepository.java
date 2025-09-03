package co.com.bancolombia.model.loans.gateways;

import java.math.BigInteger;

import co.com.bancolombia.model.loans.Loan;
import reactor.core.publisher.Mono;

public interface LoanRepository {
    Mono<Loan> findByName(String name);
    Mono<Loan> findById(BigInteger id);
}

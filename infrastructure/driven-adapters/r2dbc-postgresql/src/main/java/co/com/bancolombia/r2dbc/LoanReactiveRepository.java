package co.com.bancolombia.r2dbc;

import java.math.BigInteger;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import co.com.bancolombia.r2dbc.entity.LoanEntity;
import reactor.core.publisher.Mono;

public interface LoanReactiveRepository extends ReactiveCrudRepository<LoanEntity, BigInteger>, ReactiveQueryByExampleExecutor<LoanEntity> {
    Mono<LoanEntity> findByName(String name);
    
}
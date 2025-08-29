package co.com.bancolombia.r2dbc;

import java.math.BigInteger;
import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LoanApplicationReactiveRepository extends ReactiveCrudRepository<LoanApplicationEntity, BigInteger>, ReactiveQueryByExampleExecutor<LoanApplicationEntity> {
    
}

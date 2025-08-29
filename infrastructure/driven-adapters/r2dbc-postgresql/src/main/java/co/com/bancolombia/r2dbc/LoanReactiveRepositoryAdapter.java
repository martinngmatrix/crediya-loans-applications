package co.com.bancolombia.r2dbc;

import java.math.BigInteger;

import org.springframework.stereotype.Repository;

import co.com.bancolombia.model.loans.Loan;
import co.com.bancolombia.model.loans.gateways.LoanRepository;
import co.com.bancolombia.r2dbc.entity.LoanEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Mono;

@Repository
public class LoanReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    Loan/* change for domain model */,
    LoanEntity/* change for adapter model */,
    BigInteger,
    LoanReactiveRepository
> implements LoanRepository {
    public LoanReactiveRepositoryAdapter(
    LoanReactiveRepository repository,
    org.reactivecommons.utils.ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Loan.class/* change for domain model */));
    }

    @Override
    public Mono<Loan> findByName(String name) {
        return repository.findByName(name).map(this::toEntity);
    }

}

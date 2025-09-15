package co.com.bancolombia.r2dbc;

import java.math.BigInteger;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import co.com.bancolombia.model.loanapplication.LoanApplication;
import co.com.bancolombia.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class LoanApplicationReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    LoanApplication/* change for domain model */,
    LoanApplicationEntity/* change for adapter model */,
    BigInteger,
    LoanApplicationReactiveRepository
> implements LoanApplicationRepository {
    public LoanApplicationReactiveRepositoryAdapter(
    LoanApplicationReactiveRepository repository,
    org.reactivecommons.utils.ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, LoanApplication.class/* change for domain model */));
    }

    @Transactional
    @Override
    public Mono<LoanApplication> createLoanApplication(LoanApplication loanApplication, String documentNumber, String loanType) {
        return repository.save(toData(loanApplication)).map(this::toEntity);
    }

    @Override
    public Flux<LoanApplication> getLoansApplicationsWithPagination(String status, int size, int page) {
        int offset = (page - 1) * size;
        return repository.findByStatusWithPagination(status, size, offset).map(this::toEntity);
    }

    @Override
    public Mono<LoanApplication> updateLoanApplicationStatus(BigInteger id, String status) {
        return repository.updateStatusById(id, status).map(this::toEntity);
    }

    @Override
    public Flux<LoanApplication> getApprovedLoansApplications(BigInteger userId) {
        return repository.findApprovedLoansByUserId(userId).map(this::toEntity);
    }

    @Override
    public Mono<LoanApplication> getLoanApplicationById(BigInteger loanApplicationId) {
        return repository.findById(loanApplicationId).map(this::toEntity);
    } 
}

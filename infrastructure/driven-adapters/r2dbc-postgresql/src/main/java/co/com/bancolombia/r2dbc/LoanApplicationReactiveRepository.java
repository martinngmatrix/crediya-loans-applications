package co.com.bancolombia.r2dbc;

import java.math.BigInteger;
import java.util.Collection;

import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LoanApplicationReactiveRepository extends ReactiveCrudRepository<LoanApplicationEntity, BigInteger>, ReactiveQueryByExampleExecutor<LoanApplicationEntity> {
    Flux<LoanApplicationEntity> findAllByStatusIn(Collection<String> statuses);

    @Query("SELECT * FROM loans_applications WHERE status = :status LIMIT :size OFFSET :offset")
    Flux<LoanApplicationEntity> findByStatusWithPagination(
        @Param("status") String status,
        @Param("size") int size,
        @Param("offset") int offset
    );

    @Query("UPDATE loans_applications SET status = :status WHERE id = :id RETURNING *")
    Mono<LoanApplicationEntity> updateStatusById(@Param("id") BigInteger id, @Param("status") String status);
}

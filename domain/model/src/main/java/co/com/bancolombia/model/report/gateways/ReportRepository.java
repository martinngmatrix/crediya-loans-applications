package co.com.bancolombia.model.report.gateways;

import java.util.List;

import co.com.bancolombia.model.report.Report;
import reactor.core.publisher.Mono;

public interface ReportRepository {
    Mono<List<Report>> getEntityBySomeKeys(String partitionKey);
}

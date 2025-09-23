package co.com.bancolombia.dynamodb;

import co.com.bancolombia.dynamodb.helper.TemplateAdapterOperations;
import co.com.bancolombia.model.report.Report;
import co.com.bancolombia.model.report.gateways.ReportRepository;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;


@Repository
public class DynamoDBTemplateAdapter extends TemplateAdapterOperations<Report /*domain model*/, String, ModelEntity /*adapter model*/> implements ReportRepository  {

    public DynamoDBTemplateAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(connectionFactory, mapper, d -> mapper.map(d, Report.class /*domain model*/), "dyn-reports");
    }

    @Override
    public Mono<List<Report /*domain model*/>> getEntityBySomeKeys(String partitionKey) {
        QueryEnhancedRequest queryExpression = generateQueryExpression(partitionKey);
        return query(queryExpression);
    }

    public Mono<List<Report /*domain model*/>> getEntityBySomeKeysByIndex(String partitionKey) {
        QueryEnhancedRequest queryExpression = generateQueryExpression(partitionKey);
        return queryByIndex(queryExpression, "secondary_index" /*index is optional if you define in constructor*/);
    }

    private QueryEnhancedRequest generateQueryExpression(String partitionKey) {
        return QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(Key.builder().partitionValue(partitionKey).build()))
                .build();
    }
}

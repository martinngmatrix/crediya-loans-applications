package co.com.bancolombia.dynamodb;

import java.math.BigDecimal;
import java.math.BigInteger;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

/* Enhanced DynamoDB annotations are incompatible with Lombok #1932
         https://github.com/aws/aws-sdk-java-v2/issues/1932*/
@DynamoDbBean
public class ModelEntity {

    private String applicationStatus;
    private BigInteger count;
    private BigDecimal totalAmount;

    public ModelEntity() {
    }

    public ModelEntity(String applicationStatus, BigInteger count, BigDecimal totalAmount) {
        this.applicationStatus = applicationStatus;
        this.count = count;
        this.totalAmount = totalAmount;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("applicationStatus")
    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    @DynamoDbAttribute("count")
    public BigInteger getCount() {
        return count;
    }

    public void setCount(BigInteger count) {
        this.count = count;
    }

    @DynamoDbAttribute("totalAmount")
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}

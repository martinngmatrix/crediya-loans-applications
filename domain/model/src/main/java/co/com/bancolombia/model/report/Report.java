package co.com.bancolombia.model.report;
import lombok.Builder;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Report {
    private String applicationStatus;
    private BigDecimal totalAmount;
    private BigInteger count;
}

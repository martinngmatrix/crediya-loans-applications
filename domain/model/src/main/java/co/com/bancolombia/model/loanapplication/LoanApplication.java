package co.com.bancolombia.model.loanapplication;
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
public class LoanApplication {
    private BigInteger id;
    private BigInteger userId;
    private BigInteger loanId;
    private BigDecimal amount;
    private Integer term;
    private String status;
    private BigDecimal interestRate;
}

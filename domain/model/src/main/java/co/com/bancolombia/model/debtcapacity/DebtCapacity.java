package co.com.bancolombia.model.debtcapacity;
import lombok.Builder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DebtCapacity {
    private String result;
    private BigInteger loanApplicationId;
    private String email;
    private BigDecimal maxCapacity;
    private BigDecimal currentMonthlyDebt;
    private BigDecimal availableCapacity;
    private BigDecimal newLoanInstallment;
    private List<PaymentPlan> paymentPlan;
}
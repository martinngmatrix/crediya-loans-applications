package co.com.bancolombia.model.debtcapacity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PaymentPlan {
    private BigInteger month;
    private BigDecimal capital;          
    private BigDecimal interest;         
    private BigDecimal installment;      
    private BigDecimal remainingBalance;
}
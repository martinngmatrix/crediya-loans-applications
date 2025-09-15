package co.com.bancolombia.api.dto;

import java.math.BigInteger;

public record CalculateDebtCapacityDTO(
    BigInteger userId,
    BigInteger loanApplicationId
) {
    
}

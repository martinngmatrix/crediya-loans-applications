package co.com.bancolombia.api.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

public record ListLoanApplicationDTO (
    BigInteger id,
    String email,
    String name,
    String loanName,
    BigDecimal interestRate,
    BigDecimal deudaTotalMensualSolicitudesAprobadas,
    BigDecimal baseSalary,
    BigDecimal amount,
    Integer term,
    String status  
) {}
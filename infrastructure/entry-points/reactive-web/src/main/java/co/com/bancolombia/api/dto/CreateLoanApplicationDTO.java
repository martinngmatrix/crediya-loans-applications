package co.com.bancolombia.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

public record CreateLoanApplicationDTO (
    @NotBlank(message = "El numero de documento no puede ser nulo ni vacío")
    String documentNumber,
    BigDecimal amount,
    Integer term,
    String loanType,
    BigDecimal interestRate,
    Boolean automaticValidation
) {}

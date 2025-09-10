package co.com.bancolombia.api.dto;

import java.math.BigInteger;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateLoanApplicationDTO (
    @NotNull(message = "El id no puede ser nulo ni vacío")
    BigInteger id,
    @NotBlank(message = "El status no puede ser nulo ni vacio")
    String status
){}

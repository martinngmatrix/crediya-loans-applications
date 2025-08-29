package co.com.bancolombia.api.mapper;

import org.mapstruct.Mapper;

import co.com.bancolombia.api.dto.CreateLoanApplicationDTO;
import co.com.bancolombia.model.loanapplication.LoanApplication;

@Mapper(componentModel = "spring")
public interface LoanApplicationDTOMapper {
    LoanApplication toModel(CreateLoanApplicationDTO createLoanApplicationDTO);
}

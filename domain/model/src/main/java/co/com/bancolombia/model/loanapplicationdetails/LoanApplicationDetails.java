package co.com.bancolombia.model.loanapplicationdetails;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanApplicationDetails {

    private int page;
    private int size;
    private boolean hasNext;

    private List<LoanApplicationDetails.Item> content;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class Item {
        private BigInteger id;
        private String email;
        private String name;
        private String loanName;
        private BigDecimal interestRate;
        private BigDecimal deudaTotalMensualSolicitudesAprobadas;
        private BigDecimal baseSalary;
        private BigDecimal amount;
        private Integer term;
        private String status;
    }
}

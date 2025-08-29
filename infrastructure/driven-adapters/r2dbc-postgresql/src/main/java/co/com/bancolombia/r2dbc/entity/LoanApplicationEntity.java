package co.com.bancolombia.r2dbc.entity;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table("loans_applications")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoanApplicationEntity {

    @Id
    private BigInteger id;

    @Column("user_id")
    private BigInteger userId;

    @Column("loan_id")
    private BigInteger loanId;

    @Column("amount")
    private BigDecimal amount;

    @Column("term")
    private Integer term;

    @Column("status")
    private String status;
}

package co.com.bancolombia.r2dbc.entity;

import java.math.BigInteger;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table("loans")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoanEntity {
    @Id
    private BigInteger id;

    @Column
    private String name;
}

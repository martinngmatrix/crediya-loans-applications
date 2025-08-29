package co.com.bancolombia.model.user;
import lombok.Builder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
//import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private BigInteger id;
    private String name;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private String email;
    private String phone;
    private BigDecimal baseSalary;
    private String documentNumber;
    private String password;
    private String role;
}

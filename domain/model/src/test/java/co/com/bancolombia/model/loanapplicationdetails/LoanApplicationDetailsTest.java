package co.com.bancolombia.model.loanapplicationdetails;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class LoanApplicationDetailsTest {
    @Test
    void testAllArgsConstructorAndGetters() {
        LoanApplicationDetails details = new LoanApplicationDetails(
                BigInteger.ONE,
                "test@email.com",
                "John Doe",
                "Personal Loan",
                BigDecimal.valueOf(0),
                BigDecimal.valueOf(0),
                BigDecimal.valueOf(3000),
                BigDecimal.valueOf(15000),
                24,
                "PENDING"
        );

        assertEquals(BigInteger.ONE, details.getId());
        assertEquals("test@email.com", details.getEmail());
        assertEquals("John Doe", details.getName());
        assertEquals("Personal Loan", details.getLoanName());
        assertEquals(BigDecimal.valueOf(3000), details.getBaseSalary());
        assertEquals(BigDecimal.valueOf(15000), details.getAmount());
        assertEquals(24, details.getTerm());
        assertEquals("PENDING", details.getStatus());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        LoanApplicationDetails details = new LoanApplicationDetails();
        details.setId(BigInteger.TEN);
        details.setEmail("lucia@example.com");
        details.setName("Lucia Fernandez");
        details.setLoanName("Mortgage");
        details.setBaseSalary(BigDecimal.valueOf(5000));
        details.setAmount(BigDecimal.valueOf(100000));
        details.setTerm(120);
        details.setStatus("APPROVED");

        assertEquals(BigInteger.TEN, details.getId());
        assertEquals("lucia@example.com", details.getEmail());
        assertEquals("Lucia Fernandez", details.getName());
        assertEquals("Mortgage", details.getLoanName());
        assertEquals(BigDecimal.valueOf(5000), details.getBaseSalary());
        assertEquals(BigDecimal.valueOf(100000), details.getAmount());
        assertEquals(120, details.getTerm());
        assertEquals("APPROVED", details.getStatus());
    }

    @Test
    void testBuilder() {
        LoanApplicationDetails details = LoanApplicationDetails.builder()
                .id(BigInteger.valueOf(99))
                .email("builder@example.com")
                .name("Builder Test")
                .loanName("Car Loan")
                .baseSalary(BigDecimal.valueOf(2500))
                .amount(BigDecimal.valueOf(12000))
                .term(36)
                .status("REJECTED")
                .build();

        assertEquals(BigInteger.valueOf(99), details.getId());
        assertEquals("builder@example.com", details.getEmail());
        assertEquals("Builder Test", details.getName());
        assertEquals("Car Loan", details.getLoanName());
        assertEquals(BigDecimal.valueOf(2500), details.getBaseSalary());
        assertEquals(BigDecimal.valueOf(12000), details.getAmount());
        assertEquals(36, details.getTerm());
        assertEquals("REJECTED", details.getStatus());
    }

    @Test
    void testToBuilder() {
        LoanApplicationDetails original = LoanApplicationDetails.builder()
                .id(BigInteger.valueOf(50))
                .email("original@example.com")
                .name("Original Name")
                .loanName("Credit Card")
                .baseSalary(BigDecimal.valueOf(4000))
                .amount(BigDecimal.valueOf(5000))
                .term(6)
                .status("PENDING")
                .build();

        LoanApplicationDetails modified = original.toBuilder()
                .status("APPROVED")
                .build();

        assertEquals(original.getId(), modified.getId());
        assertEquals(original.getEmail(), modified.getEmail());
        assertEquals(original.getName(), modified.getName());
        assertEquals(original.getLoanName(), modified.getLoanName());
        assertEquals(original.getBaseSalary(), modified.getBaseSalary());
        assertEquals(original.getAmount(), modified.getAmount());
        assertEquals(original.getTerm(), modified.getTerm());
        assertEquals("APPROVED", modified.getStatus());
    }
}

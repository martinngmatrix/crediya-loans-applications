package co.com.bancolombia.model.loanapplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class LoanApplicationTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        LoanApplication loanApplication = new LoanApplication();

        loanApplication.setId(BigInteger.ONE);
        loanApplication.setUserId(BigInteger.valueOf(100));
        loanApplication.setLoanId(BigInteger.valueOf(200));
        loanApplication.setAmount(BigDecimal.valueOf(15000.75));
        loanApplication.setTerm(24);
        loanApplication.setStatus("PENDING");

        assertEquals(BigInteger.ONE, loanApplication.getId());
        assertEquals(BigInteger.valueOf(100), loanApplication.getUserId());
        assertEquals(BigInteger.valueOf(200), loanApplication.getLoanId());
        assertEquals(BigDecimal.valueOf(15000.75), loanApplication.getAmount());
        assertEquals(24, loanApplication.getTerm());
        assertEquals("PENDING", loanApplication.getStatus());
    }

    @Test
    void testAllArgsConstructor() {
        LoanApplication loanApplication = new LoanApplication(
                BigInteger.TEN,
                BigInteger.valueOf(101),
                BigInteger.valueOf(202),
                BigDecimal.valueOf(5000),
                12,
                "APPROVED",
                BigDecimal.valueOf(0)
        );

        assertEquals(BigInteger.TEN, loanApplication.getId());
        assertEquals(BigInteger.valueOf(101), loanApplication.getUserId());
        assertEquals(BigInteger.valueOf(202), loanApplication.getLoanId());
        assertEquals(BigDecimal.valueOf(5000), loanApplication.getAmount());
        assertEquals(12, loanApplication.getTerm());
        assertEquals("APPROVED", loanApplication.getStatus());
    }

    @Test
    void testBuilder() {
        LoanApplication loanApplication = LoanApplication.builder()
                .id(BigInteger.valueOf(123))
                .userId(BigInteger.valueOf(321))
                .loanId(BigInteger.valueOf(456))
                .amount(BigDecimal.valueOf(20000))
                .term(36)
                .status("REJECTED")
                .build();

        assertNotNull(loanApplication);
        assertEquals(BigInteger.valueOf(123), loanApplication.getId());
        assertEquals(BigInteger.valueOf(321), loanApplication.getUserId());
        assertEquals(BigInteger.valueOf(456), loanApplication.getLoanId());
        assertEquals(BigDecimal.valueOf(20000), loanApplication.getAmount());
        assertEquals(36, loanApplication.getTerm());
        assertEquals("REJECTED", loanApplication.getStatus());
    }

    @Test
    void testToBuilder() {
        LoanApplication loanApplication = LoanApplication.builder()
                .id(BigInteger.valueOf(1))
                .userId(BigInteger.valueOf(2))
                .loanId(BigInteger.valueOf(3))
                .amount(BigDecimal.valueOf(10000))
                .term(48)
                .status("PENDING")
                .build();

        LoanApplication modified = loanApplication.toBuilder()
                .status("APPROVED")
                .build();

        assertEquals(BigInteger.valueOf(1), modified.getId());
        assertEquals(BigInteger.valueOf(2), modified.getUserId());
        assertEquals(BigInteger.valueOf(3), modified.getLoanId());
        assertEquals(BigDecimal.valueOf(10000), modified.getAmount());
        assertEquals(48, modified.getTerm());
        assertEquals("APPROVED", modified.getStatus());
    }
}

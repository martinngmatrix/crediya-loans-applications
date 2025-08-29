package co.com.bancolombia.model.loans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

public class LoanTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Loan loan = new Loan();
        loan.setId(BigInteger.ONE);
        loan.setName("Personal Loan");

        assertEquals(BigInteger.ONE, loan.getId());
        assertEquals("Personal Loan", loan.getName());
    }

    @Test
    void testAllArgsConstructor() {
        Loan loan = new Loan(BigInteger.TEN, "Mortgage Loan");

        assertEquals(BigInteger.TEN, loan.getId());
        assertEquals("Mortgage Loan", loan.getName());
    }

    @Test
    void testBuilder() {
        Loan loan = Loan.builder()
                .id(BigInteger.valueOf(100))
                .name("Car Loan")
                .build();

        assertEquals(BigInteger.valueOf(100), loan.getId());
        assertEquals("Car Loan", loan.getName());
    }

    @Test
    void testToBuilder() {
        Loan loan = Loan.builder()
                .id(BigInteger.valueOf(200))
                .name("Education Loan")
                .build();

        Loan updatedLoan = loan.toBuilder()
                .name("Updated Education Loan")
                .build();

        assertEquals(BigInteger.valueOf(200), updatedLoan.getId());
        assertEquals("Updated Education Loan", updatedLoan.getName());
    }

    @Test
    void testSettersOverrideValues() {
        Loan loan = new Loan();
        loan.setId(BigInteger.valueOf(1));
        loan.setName("Initial");

        loan.setId(BigInteger.valueOf(2));
        loan.setName("Updated");

        assertEquals(BigInteger.valueOf(2), loan.getId());
        assertEquals("Updated", loan.getName());
    }
}

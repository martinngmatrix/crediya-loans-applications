package co.com.bancolombia.model.debtcapacity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.junit.jupiter.api.Test;

class DebtCapacityTest {

    @Test
    void shouldBuildDebtCapacityWithBuilder() {
        DebtCapacity debtCapacity = DebtCapacity.builder()
                .result("APROBADO")
                .loanApplicationId(BigInteger.valueOf(1))
                .email("test@email.com")
                .maxCapacity(new BigDecimal("5000"))
                .currentMonthlyDebt(new BigDecimal("1500"))
                .availableCapacity(new BigDecimal("3500"))
                .newLoanInstallment(new BigDecimal("1200"))
                .paymentPlan(List.of())
                .build();

        assertEquals("APROBADO", debtCapacity.getResult());
        assertEquals(BigInteger.valueOf(1), debtCapacity.getLoanApplicationId());
        assertEquals("test@email.com", debtCapacity.getEmail());
        assertEquals(new BigDecimal("5000"), debtCapacity.getMaxCapacity());
        assertEquals(new BigDecimal("1500"), debtCapacity.getCurrentMonthlyDebt());
        assertEquals(new BigDecimal("3500"), debtCapacity.getAvailableCapacity());
        assertEquals(new BigDecimal("1200"), debtCapacity.getNewLoanInstallment());
        assertEquals(List.of(), debtCapacity.getPaymentPlan());
    }

    @Test
    void shouldUseSettersAndGetters() {
        DebtCapacity debtCapacity = new DebtCapacity();

        debtCapacity.setResult("RECHAZADO");
        debtCapacity.setLoanApplicationId(BigInteger.TEN);
        debtCapacity.setEmail("otro@email.com");
        debtCapacity.setMaxCapacity(new BigDecimal("4000"));
        debtCapacity.setCurrentMonthlyDebt(new BigDecimal("1000"));
        debtCapacity.setAvailableCapacity(new BigDecimal("3000"));
        debtCapacity.setNewLoanInstallment(new BigDecimal("800"));
        debtCapacity.setPaymentPlan(null);

        assertEquals("RECHAZADO", debtCapacity.getResult());
        assertEquals(BigInteger.TEN, debtCapacity.getLoanApplicationId());
        assertEquals("otro@email.com", debtCapacity.getEmail());
        assertEquals(new BigDecimal("4000"), debtCapacity.getMaxCapacity());
        assertEquals(new BigDecimal("1000"), debtCapacity.getCurrentMonthlyDebt());
        assertEquals(new BigDecimal("3000"), debtCapacity.getAvailableCapacity());
        assertEquals(new BigDecimal("800"), debtCapacity.getNewLoanInstallment());
        assertNull(debtCapacity.getPaymentPlan());
    }

    @Test
    void shouldCreateDebtCapacityWithAllArgsConstructor() {
        DebtCapacity debtCapacity = new DebtCapacity(
                "APROBADO",
                BigInteger.ONE,
                "mail@test.com",
                new BigDecimal("6000"),
                new BigDecimal("2000"),
                new BigDecimal("4000"),
                new BigDecimal("1000"),
                null
        );

        assertEquals("APROBADO", debtCapacity.getResult());
        assertEquals(BigInteger.ONE, debtCapacity.getLoanApplicationId());
        assertEquals("mail@test.com", debtCapacity.getEmail());
        assertEquals(new BigDecimal("6000"), debtCapacity.getMaxCapacity());
        assertEquals(new BigDecimal("2000"), debtCapacity.getCurrentMonthlyDebt());
        assertEquals(new BigDecimal("4000"), debtCapacity.getAvailableCapacity());
        assertEquals(new BigDecimal("1000"), debtCapacity.getNewLoanInstallment());
        assertNull(debtCapacity.getPaymentPlan());
    }

    @Test
    void shouldCopyDebtCapacityWithToBuilder() {
        DebtCapacity debtCapacity = DebtCapacity.builder()
                .result("APROBADO")
                .loanApplicationId(BigInteger.ONE)
                .email("test@email.com")
                .maxCapacity(new BigDecimal("5000"))
                .currentMonthlyDebt(new BigDecimal("1500"))
                .availableCapacity(new BigDecimal("3500"))
                .newLoanInstallment(new BigDecimal("1200"))
                .paymentPlan(List.of())
                .build();

        DebtCapacity modified = debtCapacity.toBuilder()
                .result("RECHAZADO")
                .build();

        assertEquals("RECHAZADO", modified.getResult());
        assertEquals(new BigDecimal("5000"), modified.getMaxCapacity());
        assertEquals(new BigDecimal("1500"), modified.getCurrentMonthlyDebt());
        assertEquals(new BigDecimal("3500"), modified.getAvailableCapacity());
        assertEquals(new BigDecimal("1200"), modified.getNewLoanInstallment());
        assertEquals(BigInteger.ONE, modified.getLoanApplicationId());
        assertEquals("test@email.com", modified.getEmail());
        assertEquals(List.of(), modified.getPaymentPlan());
    }

    @Test
    void shouldAllowNullValues() {
        DebtCapacity debtCapacity = new DebtCapacity(null, null, null, null, null, null, null, null);
        assertNull(debtCapacity.getResult());
        assertNull(debtCapacity.getLoanApplicationId());
        assertNull(debtCapacity.getEmail());
        assertNull(debtCapacity.getMaxCapacity());
        assertNull(debtCapacity.getCurrentMonthlyDebt());
        assertNull(debtCapacity.getAvailableCapacity());
        assertNull(debtCapacity.getNewLoanInstallment());
        assertNull(debtCapacity.getPaymentPlan());
    }
}
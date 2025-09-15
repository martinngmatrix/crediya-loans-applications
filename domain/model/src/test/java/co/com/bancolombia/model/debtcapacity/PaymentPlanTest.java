package co.com.bancolombia.model.debtcapacity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class PaymentPlanTest {

    @Test
    void shouldBuildPaymentPlanWithBuilder() {
        PaymentPlan paymentPlan = PaymentPlan.builder()
                .month(BigInteger.ONE)
                .capital(new BigDecimal("1000"))
                .interest(new BigDecimal("200"))
                .installment(new BigDecimal("1200"))
                .remainingBalance(new BigDecimal("4000"))
                .build();

        assertEquals(BigInteger.ONE, paymentPlan.getMonth());
        assertEquals(new BigDecimal("1000"), paymentPlan.getCapital());
        assertEquals(new BigDecimal("200"), paymentPlan.getInterest());
        assertEquals(new BigDecimal("1200"), paymentPlan.getInstallment());
        assertEquals(new BigDecimal("4000"), paymentPlan.getRemainingBalance());
    }

    @Test
    void shouldUseSettersAndGetters() {
        PaymentPlan paymentPlan = new PaymentPlan();

        paymentPlan.setMonth(BigInteger.TEN);
        paymentPlan.setCapital(new BigDecimal("1500"));
        paymentPlan.setInterest(new BigDecimal("300"));
        paymentPlan.setInstallment(new BigDecimal("1800"));
        paymentPlan.setRemainingBalance(new BigDecimal("2500"));

        assertEquals(BigInteger.TEN, paymentPlan.getMonth());
        assertEquals(new BigDecimal("1500"), paymentPlan.getCapital());
        assertEquals(new BigDecimal("300"), paymentPlan.getInterest());
        assertEquals(new BigDecimal("1800"), paymentPlan.getInstallment());
        assertEquals(new BigDecimal("2500"), paymentPlan.getRemainingBalance());
    }

    @Test
    void shouldCreatePaymentPlanWithAllArgsConstructor() {
        PaymentPlan paymentPlan = new PaymentPlan(
                BigInteger.valueOf(5),
                new BigDecimal("2000"),
                new BigDecimal("400"),
                new BigDecimal("2400"),
                new BigDecimal("10000")
        );

        assertEquals(BigInteger.valueOf(5), paymentPlan.getMonth());
        assertEquals(new BigDecimal("2000"), paymentPlan.getCapital());
        assertEquals(new BigDecimal("400"), paymentPlan.getInterest());
        assertEquals(new BigDecimal("2400"), paymentPlan.getInstallment());
        assertEquals(new BigDecimal("10000"), paymentPlan.getRemainingBalance());
    }

    @Test
    void shouldCopyPaymentPlanWithToBuilder() {
        PaymentPlan paymentPlan = PaymentPlan.builder()
                .month(BigInteger.ONE)
                .capital(new BigDecimal("1000"))
                .interest(new BigDecimal("200"))
                .installment(new BigDecimal("1200"))
                .remainingBalance(new BigDecimal("4000"))
                .build();

        PaymentPlan modified = paymentPlan.toBuilder()
                .interest(new BigDecimal("250"))
                .build();

        assertEquals(BigInteger.ONE, modified.getMonth());
        assertEquals(new BigDecimal("1000"), modified.getCapital());
        assertEquals(new BigDecimal("250"), modified.getInterest()); // cambiado
        assertEquals(new BigDecimal("1200"), modified.getInstallment());
        assertEquals(new BigDecimal("4000"), modified.getRemainingBalance());
    }

    @Test
    void shouldAllowNullValues() {
        PaymentPlan paymentPlan = new PaymentPlan(null, null, null, null, null);

        assertNull(paymentPlan.getMonth());
        assertNull(paymentPlan.getCapital());
        assertNull(paymentPlan.getInterest());
        assertNull(paymentPlan.getInstallment());
        assertNull(paymentPlan.getRemainingBalance());
    }
}

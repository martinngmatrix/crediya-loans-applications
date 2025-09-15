package co.com.bancolombia.usecase.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.junit.jupiter.api.Test;

import co.com.bancolombia.model.debtcapacity.DebtCapacity;
import co.com.bancolombia.model.debtcapacity.PaymentPlan;
import co.com.bancolombia.utils.HtmlUtil;

class HtmlUtilTest {

    @Test
    void testGeneratePaymentPlanHtml() {
        PaymentPlan entry1 = PaymentPlan.builder()
                .month(BigInteger.valueOf(1))
                .installment(new BigDecimal("1500.75"))
                .capital(new BigDecimal("1000.50"))
                .interest(new BigDecimal("500.25"))
                .remainingBalance(new BigDecimal("9000.00"))
                .build();

        PaymentPlan entry2 = PaymentPlan.builder()
                .month(BigInteger.valueOf(2))
                .installment(new BigDecimal("1500.75"))
                .capital(new BigDecimal("1050.00"))
                .interest(new BigDecimal("450.75"))
                .remainingBalance(new BigDecimal("7950.00"))
                .build();

        DebtCapacity debtCapacity = DebtCapacity.builder()
                .loanApplicationId(BigInteger.valueOf(123))
                .result("Aprobado")
                .newLoanInstallment(new BigDecimal("1500.75"))
                .paymentPlan(List.of(entry1, entry2))
                .build();

        String html = HtmlUtil.generatePaymentPlanHtml(debtCapacity);

        assertTrue(html.contains("Plan de Pago - Solicitud #123"));
        assertTrue(html.contains("Estado: Aprobado"));
        assertTrue(html.contains("1500.75"));
        assertTrue(html.contains("1000.50"));
        assertTrue(html.contains("500.25")); 
        assertTrue(html.contains("9000.00"));
        assertTrue(html.contains("<table")); 
        assertTrue(html.contains("</table>"));
    }

    @Test
    void testGenerateStatusHtml() {
        BigInteger loanId = BigInteger.valueOf(456);
        String status = "Rechazado";

        String html = HtmlUtil.generateStatusHtml(loanId, status);

        assertTrue(html.contains("Estado de su Solicitud"));
        assertTrue(html.contains("456"));
        assertTrue(html.contains("Rechazado"));
        assertTrue(html.contains("Gracias por confiar en nosotros"));
    }
}
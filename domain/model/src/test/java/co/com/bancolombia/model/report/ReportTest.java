package co.com.bancolombia.model.report;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class ReportTest {
    @Test
    void testNoArgsConstructorAndSetters() {
        Report report = new Report();
        report.setApplicationStatus("APPROVED");
        report.setTotalAmount(BigDecimal.valueOf(5000));
        report.setCount(BigInteger.valueOf(10));

        assertEquals("APPROVED", report.getApplicationStatus());
        assertEquals(BigDecimal.valueOf(5000), report.getTotalAmount());
        assertEquals(BigInteger.valueOf(10), report.getCount());
    }

    @Test
    void testAllArgsConstructor() {
        Report report = new Report("PENDING", BigDecimal.valueOf(2000), BigInteger.valueOf(5));

        assertEquals("PENDING", report.getApplicationStatus());
        assertEquals(BigDecimal.valueOf(2000), report.getTotalAmount());
        assertEquals(BigInteger.valueOf(5), report.getCount());
    }

    @Test
    void testBuilder() {
        Report report = Report.builder()
                .applicationStatus("REJECTED")
                .totalAmount(BigDecimal.valueOf(1000))
                .count(BigInteger.ONE)
                .build();

        assertEquals("REJECTED", report.getApplicationStatus());
        assertEquals(BigDecimal.valueOf(1000), report.getTotalAmount());
        assertEquals(BigInteger.ONE, report.getCount());
    }

    @Test
    void testToBuilder() {
        Report original = Report.builder()
                .applicationStatus("APPROVED")
                .totalAmount(BigDecimal.valueOf(5000))
                .count(BigInteger.TEN)
                .build();

        Report modified = original.toBuilder()
                .applicationStatus("REJECTED")
                .build();

        assertEquals("REJECTED", modified.getApplicationStatus());
        assertEquals(BigDecimal.valueOf(5000), modified.getTotalAmount());
        assertEquals(BigInteger.TEN, modified.getCount());
    }
}

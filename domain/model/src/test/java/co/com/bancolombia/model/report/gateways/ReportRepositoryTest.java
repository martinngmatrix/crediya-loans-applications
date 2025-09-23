package co.com.bancolombia.model.report.gateways;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import co.com.bancolombia.model.report.Report;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ReportRepositoryTest {

    private ReportRepository reportRepository;

    @BeforeEach
    void setUp() {
        reportRepository = Mockito.mock(ReportRepository.class);
    }

    @Test
    void testGetEntityBySomeKeysReturnsReports() {
        Report report = Report.builder()
                .applicationStatus("APPROVED")
                .count(BigInteger.valueOf(5))
                .totalAmount(BigDecimal.valueOf(12000))
                .build();

        when(reportRepository.getEntityBySomeKeys("APPROVED"))
                .thenReturn(Mono.just(List.of(report)));

        StepVerifier.create(reportRepository.getEntityBySomeKeys("APPROVED"))
                .assertNext(reports -> {
                    assertEquals(1, reports.size());
                    assertEquals("APPROVED", reports.get(0).getApplicationStatus());
                    assertEquals(BigInteger.valueOf(5), reports.get(0).getCount());
                    assertEquals(BigDecimal.valueOf(12000), reports.get(0).getTotalAmount());
                })
                .verifyComplete();

        verify(reportRepository).getEntityBySomeKeys("APPROVED");
    }

    @Test
    void testGetEntityBySomeKeysReturnsEmpty() {
        when(reportRepository.getEntityBySomeKeys("REJECTED"))
                .thenReturn(Mono.just(List.of()));

        StepVerifier.create(reportRepository.getEntityBySomeKeys("REJECTED"))
                .assertNext(reports -> assertTrue(reports.isEmpty()))
                .verifyComplete();

        verify(reportRepository).getEntityBySomeKeys("REJECTED");
    }

    @Test
    void testGetEntityBySomeKeysError() {
        when(reportRepository.getEntityBySomeKeys("ERROR"))
                .thenReturn(Mono.error(new RuntimeException("Database unavailable")));

        StepVerifier.create(reportRepository.getEntityBySomeKeys("ERROR"))
                .expectErrorMatches(err -> err instanceof RuntimeException &&
                                           err.getMessage().equals("Database unavailable"))
                .verify();

        verify(reportRepository).getEntityBySomeKeys("ERROR");
    }
}

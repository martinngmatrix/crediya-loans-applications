package co.com.bancolombia.model.loanapplication.constants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

class ConstantsTest {

    @Test
    void shouldHaveCorrectPendingReviewMessage() {
        assertEquals("Pendiente de revisión", Constants.PENDING_REVIEW);
    }

    @Test
    void shouldHaveCorrectLoanTypesToReviewMessage() {
        assertEquals(List.of("Pendiente de revisión", "Rechazado", "Revision manual"), Constants.LOAN_TYPES_TO_REVIEW);
    }

    @Test
    void constantShouldNotBeNullOrEmpty() {
        assertNotNull(Constants.PENDING_REVIEW);
        assertNotNull(Constants.LOAN_TYPES_TO_REVIEW);
        assertFalse(Constants.PENDING_REVIEW.isEmpty());
        assertFalse(Constants.LOAN_TYPES_TO_REVIEW.isEmpty());
    }
}

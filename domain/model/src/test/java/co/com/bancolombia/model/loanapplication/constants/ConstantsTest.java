package co.com.bancolombia.model.loanapplication.constants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class ConstantsTest {

    @Test
    void shouldHaveCorrectPendingReviewMessage() {
        assertEquals("Pendiente de revisión", Constants.PENDING_REVIEW);
    }

    @Test
    void constantShouldNotBeNullOrEmpty() {
        assertNotNull(Constants.PENDING_REVIEW);
        assertFalse(Constants.PENDING_REVIEW.isEmpty());
    }
}

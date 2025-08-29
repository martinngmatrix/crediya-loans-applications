package co.com.bancolombia.model.loanapplication.constants.messages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class LoanApplicationErrorMessagesTest {

    @Test
    void shouldHaveCorrectUserNotFoundMessage() {
        assertEquals("Usuario no encontrado", LoanApplicationErrorMessages.USER_NOT_FOUND);
    }

    @Test
    void shouldHaveCorrectLoanTypeNotValidMessage() {
        assertEquals("Tipo de préstamo no válido", LoanApplicationErrorMessages.LOAN_TYPE_NOT_VALID);
    }

    @Test
    void constantsShouldNotBeNullOrEmpty() {
        assertNotNull(LoanApplicationErrorMessages.USER_NOT_FOUND);
        assertFalse(LoanApplicationErrorMessages.USER_NOT_FOUND.isEmpty());

        assertNotNull(LoanApplicationErrorMessages.LOAN_TYPE_NOT_VALID);
        assertFalse(LoanApplicationErrorMessages.LOAN_TYPE_NOT_VALID.isEmpty());
    }
}

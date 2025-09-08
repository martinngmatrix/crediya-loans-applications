package co.com.bancolombia.model.loanapplicationdetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.junit.jupiter.api.Test;

class LoanApplicationDetailsTest {

    @Test
    void testItemAllArgsConstructorAndGetters() {
        LoanApplicationDetails.Item item = new LoanApplicationDetails.Item(
                BigInteger.ONE,
                "test@email.com",
                "John Doe",
                "Personal Loan",
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.valueOf(3000),
                BigDecimal.valueOf(15000),
                24,
                "PENDING"
        );

        assertEquals(BigInteger.ONE, item.getId());
        assertEquals("test@email.com", item.getEmail());
        assertEquals("John Doe", item.getName());
        assertEquals("Personal Loan", item.getLoanName());
        assertEquals(BigDecimal.valueOf(3000), item.getBaseSalary());
        assertEquals(BigDecimal.valueOf(15000), item.getAmount());
        assertEquals(24, item.getTerm());
        assertEquals("PENDING", item.getStatus());
    }

    @Test
    void testItemNoArgsConstructorAndSetters() {
        LoanApplicationDetails.Item item = new LoanApplicationDetails.Item();
        item.setId(BigInteger.TEN);
        item.setEmail("lucia@example.com");
        item.setName("Lucia Fernandez");
        item.setLoanName("Mortgage");
        item.setBaseSalary(BigDecimal.valueOf(5000));
        item.setAmount(BigDecimal.valueOf(100000));
        item.setTerm(120);
        item.setStatus("APPROVED");

        assertEquals(BigInteger.TEN, item.getId());
        assertEquals("lucia@example.com", item.getEmail());
        assertEquals("Lucia Fernandez", item.getName());
        assertEquals("Mortgage", item.getLoanName());
        assertEquals(BigDecimal.valueOf(5000), item.getBaseSalary());
        assertEquals(BigDecimal.valueOf(100000), item.getAmount());
        assertEquals(120, item.getTerm());
        assertEquals("APPROVED", item.getStatus());
    }

    @Test
    void testItemBuilder() {
        LoanApplicationDetails.Item item = LoanApplicationDetails.Item.builder()
                .id(BigInteger.valueOf(99))
                .email("builder@example.com")
                .name("Builder Test")
                .loanName("Car Loan")
                .baseSalary(BigDecimal.valueOf(2500))
                .amount(BigDecimal.valueOf(12000))
                .term(36)
                .status("REJECTED")
                .build();

        assertEquals(BigInteger.valueOf(99), item.getId());
        assertEquals("builder@example.com", item.getEmail());
        assertEquals("Builder Test", item.getName());
        assertEquals("Car Loan", item.getLoanName());
        assertEquals(BigDecimal.valueOf(2500), item.getBaseSalary());
        assertEquals(BigDecimal.valueOf(12000), item.getAmount());
        assertEquals(36, item.getTerm());
        assertEquals("REJECTED", item.getStatus());
    }

    @Test
    void testItemToBuilder() {
        LoanApplicationDetails.Item original = LoanApplicationDetails.Item.builder()
                .id(BigInteger.valueOf(50))
                .email("original@example.com")
                .name("Original Name")
                .loanName("Credit Card")
                .baseSalary(BigDecimal.valueOf(4000))
                .amount(BigDecimal.valueOf(5000))
                .term(6)
                .status("PENDING")
                .build();

        LoanApplicationDetails.Item modified = original.toBuilder()
                .status("APPROVED")
                .build();

        assertEquals(original.getId(), modified.getId());
        assertEquals(original.getEmail(), modified.getEmail());
        assertEquals(original.getName(), modified.getName());
        assertEquals(original.getLoanName(), modified.getLoanName());
        assertEquals(original.getBaseSalary(), modified.getBaseSalary());
        assertEquals(original.getAmount(), modified.getAmount());
        assertEquals(original.getTerm(), modified.getTerm());
        assertEquals("APPROVED", modified.getStatus());
    }

    @Test
    void testLoanApplicationDetailsWithPagination() {
        LoanApplicationDetails.Item item = LoanApplicationDetails.Item.builder()
                .id(BigInteger.ONE)
                .email("page@test.com")
                .name("Page User")
                .loanName("Home Loan")
                .amount(BigDecimal.valueOf(50000))
                .term(240)
                .status("PENDING")
                .build();

        LoanApplicationDetails details = LoanApplicationDetails.builder()
                .page(1)
                .size(1)
                .hasNext(false)
                .content(List.of(item))
                .build();

        assertEquals(1, details.getPage());
        assertEquals(1, details.getSize());
        assertTrue(!details.isHasNext());
        assertEquals(1, details.getContent().size());
        assertEquals("Page User", details.getContent().get(0).getName());
    }
}

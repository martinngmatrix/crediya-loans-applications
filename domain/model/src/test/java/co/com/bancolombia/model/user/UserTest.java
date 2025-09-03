package co.com.bancolombia.model.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    void testNoArgsConstructorAndSetters() {
        User user = new User();
        user.setName("Juan");
        user.setLastName("Pérez");
        user.setDateOfBirth(LocalDate.of(1990, 5, 10));
        user.setAddress("Calle Falsa 123");
        user.setEmail("juan@example.com");
        user.setPhone("123456789");
        user.setBaseSalary(BigDecimal.valueOf(2500.50));
        user.setDocumentNumber("987654321");

        assertEquals("Juan", user.getName());
        assertEquals("Pérez", user.getLastName());
        assertEquals(LocalDate.of(1990, 5, 10), user.getDateOfBirth());
        assertEquals("Calle Falsa 123", user.getAddress());
        assertEquals("juan@example.com", user.getEmail());
        assertEquals("123456789", user.getPhone());
        assertEquals(BigDecimal.valueOf(2500.50), user.getBaseSalary());
        assertEquals("987654321", user.getDocumentNumber());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDate dob = LocalDate.of(1995, 8, 20);
        BigDecimal salary = BigDecimal.valueOf(3000.75);

        User user = new User(
                new BigInteger("1"),
                "Ana",
                "García",
                dob,
                "Av. Principal 456",
                "ana@example.com",
                "987654321",
                salary,
                "123456789",
                "securePassword",
                "USER"
        );

        assertEquals("Ana", user.getName());
        assertEquals("García", user.getLastName());
        assertEquals(dob, user.getDateOfBirth());
        assertEquals("Av. Principal 456", user.getAddress());
        assertEquals("ana@example.com", user.getEmail());
        assertEquals("987654321", user.getPhone());
        assertEquals(salary, user.getBaseSalary());
        assertEquals("123456789", user.getDocumentNumber());
    }

    @Test
    void testBuilder() {
        User user = User.builder()
                .name("Carlos")
                .lastName("Ramírez")
                .dateOfBirth(LocalDate.of(1988, 3, 15))
                .address("Calle 10 #20-30")
                .email("carlos@example.com")
                .phone("111222333")
                .baseSalary(BigDecimal.valueOf(4500))
                .documentNumber("1122334455")
                .build();

        assertNotNull(user);
        assertEquals("Carlos", user.getName());
        assertEquals("Ramírez", user.getLastName());
        assertEquals(LocalDate.of(1988, 3, 15), user.getDateOfBirth());
        assertEquals("Calle 10 #20-30", user.getAddress());
        assertEquals("carlos@example.com", user.getEmail());
        assertEquals("111222333", user.getPhone());
        assertEquals(BigDecimal.valueOf(4500), user.getBaseSalary());
        assertEquals("1122334455", user.getDocumentNumber());
    }

    @Test
    void testToBuilder() {
        User user = User.builder()
                .name("Laura")
                .lastName("Mendoza")
                .dateOfBirth(LocalDate.of(1992, 12, 1))
                .address("Carrera 45 #67-89")
                .email("laura@example.com")
                .phone("555666777")
                .baseSalary(BigDecimal.valueOf(5200))
                .documentNumber("9988776655")
                .build();

        User updatedUser = user.toBuilder()
                .email("laura.new@example.com")
                .build();

        assertEquals("Laura", updatedUser.getName());
        assertEquals("Mendoza", updatedUser.getLastName());
        assertEquals(LocalDate.of(1992, 12, 1), updatedUser.getDateOfBirth());
        assertEquals("Carrera 45 #67-89", updatedUser.getAddress());
        assertEquals("laura.new@example.com", updatedUser.getEmail());
        assertEquals("555666777", updatedUser.getPhone());
        assertEquals(BigDecimal.valueOf(5200), updatedUser.getBaseSalary());
        assertEquals("9988776655", updatedUser.getDocumentNumber());
    }
}

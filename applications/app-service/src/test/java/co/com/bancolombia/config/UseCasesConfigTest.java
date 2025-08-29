package co.com.bancolombia.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
@ExtendWith(MockitoExtension.class)
class UseCasesConfigTest {

    @Test
    void testConfigurationClassExists() {
        assertNotNull(UseCasesConfig.class, "UseCasesConfig class should exist");
        assertTrue(UseCasesConfig.class.isAnnotationPresent(org.springframework.context.annotation.Configuration.class),
                "UseCasesConfig should be annotated with @Configuration");
    }

    @Test
    void testComponentScanConfiguration() {
        ComponentScan componentScan = UseCasesConfig.class.getAnnotation(ComponentScan.class);

        assertNotNull(componentScan, "ComponentScan annotation should be present");
        assertArrayEquals(new String[]{"co.com.bancolombia.usecase"},
                componentScan.basePackages(),
                "Base package should be co.com.bancolombia.usecase");
        assertFalse(componentScan.useDefaultFilters(), "Default filters should be disabled");
    }

    @Test
    void testRegexFilterPattern() {
        ComponentScan componentScan = UseCasesConfig.class.getAnnotation(ComponentScan.class);
        ComponentScan.Filter[] filters = componentScan.includeFilters();

        assertEquals(1, filters.length, "Should have exactly one include filter");
        assertEquals(FilterType.REGEX, filters[0].type(), "Filter type should be REGEX");
        assertEquals("^.+UseCase$", filters[0].pattern()[0], "Regex pattern should match UseCase suffix");
    }

    @Test
    void testRegexPatternMatching() {
        String regexPattern = "^.+UseCase$";

        assertTrue("MyUseCase".matches(regexPattern), "MyUseCase should match the pattern");
        assertTrue("CreateUserUseCase".matches(regexPattern), "CreateUserUseCase should match the pattern");
        assertTrue("ValidateDataUseCase".matches(regexPattern), "ValidateDataUseCase should match the pattern");
        assertTrue("aUseCase".matches(regexPattern), "aUseCase should match the pattern");

        assertFalse("UseCase".matches(regexPattern), "UseCase alone should not match the pattern");
        assertFalse("MyUseCaseImpl".matches(regexPattern), "MyUseCaseImpl should not match the pattern");
        assertFalse("MyService".matches(regexPattern), "MyService should not match the pattern");
        assertFalse("UseCase123".matches(regexPattern), "UseCase123 should not match the pattern");
        assertFalse("useCaseSmall".matches(regexPattern), "useCaseSmall should not match the pattern");
        assertFalse("".matches(regexPattern), "Empty string should not match the pattern");
    }

    @Test
    void testConfigurationInstantiation() {
        assertDoesNotThrow(() -> {
            UseCasesConfig config = new UseCasesConfig();
            assertNotNull(config, "UseCasesConfig should be instantiable");
        }, "UseCasesConfig should be instantiable without throwing exceptions");
    }

    @Test
    void testFilterTypeIsCorrect() {
        ComponentScan componentScan = UseCasesConfig.class.getAnnotation(ComponentScan.class);
        ComponentScan.Filter filter = componentScan.includeFilters()[0];

        assertEquals(FilterType.REGEX, filter.type(),
                "Filter type should be REGEX for pattern matching");
    }

    @Test
    void testBasePackageIsSpecific() {
        ComponentScan componentScan = UseCasesConfig.class.getAnnotation(ComponentScan.class);
        String[] basePackages = componentScan.basePackages();

        assertEquals(1, basePackages.length, "Should scan exactly one base package");
        assertTrue(basePackages[0].contains("usecase"),
                "Base package should contain 'usecase' in the name");
        assertTrue(basePackages[0].startsWith("co.com.bancolombia"),
                "Base package should start with company package structure");
    }

    @Test
    void testDefaultFiltersDisabled() {
        ComponentScan componentScan = UseCasesConfig.class.getAnnotation(ComponentScan.class);

        assertFalse(componentScan.useDefaultFilters(),
                "Default filters should be disabled to use only custom regex filter");
    }

    @Test
    void testOnlyIncludeFiltersConfigured() {
        ComponentScan componentScan = UseCasesConfig.class.getAnnotation(ComponentScan.class);

        assertTrue(componentScan.includeFilters().length > 0,
                "Should have include filters configured");
        assertEquals(0, componentScan.excludeFilters().length,
                "Should not have exclude filters configured");
    }
}
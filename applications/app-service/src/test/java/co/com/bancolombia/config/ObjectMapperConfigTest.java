package co.com.bancolombia.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.reactivecommons.utils.ObjectMapperImp;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ObjectMapperConfigTest {

    @Test
    void shouldReturnObjectMapperImpInstance() {
        ObjectMapperConfig config = new ObjectMapperConfig();

        ObjectMapper objectMapper = config.objectMapper();

        assertNotNull(objectMapper);
        assertTrue(objectMapper instanceof ObjectMapperImp);
    }
}
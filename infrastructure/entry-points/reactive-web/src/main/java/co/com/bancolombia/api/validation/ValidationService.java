package co.com.bancolombia.api.validation;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import co.com.bancolombia.api.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ValidationService {
    private final Validator validator;

    public <T> Mono<T> validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            List<Map<String, String>> errors = violations.stream()
                    .map(v -> Map.of("field", v.getPropertyPath().toString(),
                                     "message", v.getMessage()))
                    .toList();

            return Mono.error(new ValidationException(errors));
        }
        return Mono.just(object);
    }
}
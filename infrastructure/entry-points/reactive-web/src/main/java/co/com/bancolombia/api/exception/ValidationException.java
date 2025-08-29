package co.com.bancolombia.api.exception;

import java.util.List;
import java.util.Map;

public class ValidationException extends RuntimeException {
    private final List<Map<String, String>> errors;

    public ValidationException(List<Map<String, String>> errors) {
        super(errors.toString());
        this.errors = errors;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }
}

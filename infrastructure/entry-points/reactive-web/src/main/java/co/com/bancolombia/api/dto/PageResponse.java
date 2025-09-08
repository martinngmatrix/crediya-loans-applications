package co.com.bancolombia.api.dto;
import java.util.List;

import lombok.Builder;

@Builder(toBuilder = true)
public record PageResponse<T>(
        int page,
        int size,
        long totalElements,
        boolean hasNext,
        List<T> content
) {}
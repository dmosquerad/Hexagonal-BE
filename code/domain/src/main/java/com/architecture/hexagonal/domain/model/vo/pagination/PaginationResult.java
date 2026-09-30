package com.architecture.hexagonal.domain.model.vo.pagination;

import java.util.List;
import lombok.Builder;

@Builder
public record PaginationResult<T>(
    List<T> data, long totalElements, int totalPages, int page, int size) {}

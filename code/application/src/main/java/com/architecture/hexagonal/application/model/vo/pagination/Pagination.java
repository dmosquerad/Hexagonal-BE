package com.architecture.hexagonal.application.model.vo.pagination;

import jakarta.validation.constraints.DecimalMin;
import lombok.Builder;

@Builder
public record Pagination(@DecimalMin("0") Integer page, @DecimalMin("1") Integer size) {}

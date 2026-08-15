package com.architecture.hexagonal.domain.model.aggregate.pagination;

import lombok.Builder;

@Builder
public record Pagination(Integer page, Integer size) {}

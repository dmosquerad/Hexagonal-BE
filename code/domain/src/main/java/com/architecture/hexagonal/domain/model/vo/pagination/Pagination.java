package com.architecture.hexagonal.domain.model.vo.pagination;

import lombok.Builder;

@Builder
public record Pagination(Integer page, Integer size) {}

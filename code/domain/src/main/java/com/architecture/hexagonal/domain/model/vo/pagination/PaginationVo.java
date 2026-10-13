package com.architecture.hexagonal.domain.model.vo.pagination;

import lombok.Builder;

@Builder
public record PaginationVo(Integer page, Integer size) {}

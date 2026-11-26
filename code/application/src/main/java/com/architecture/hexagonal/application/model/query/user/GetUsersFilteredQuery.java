package com.architecture.hexagonal.application.model.query.user;

import com.architecture.hexagonal.application.model.vo.pagination.Pagination;
import jakarta.validation.Valid;
import lombok.Builder;

@Builder
public record GetUsersFilteredQuery(
    String host, Boolean blockEmail, @Valid Pagination pagination) {}

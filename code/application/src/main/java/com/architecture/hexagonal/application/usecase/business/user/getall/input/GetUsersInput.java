package com.architecture.hexagonal.application.usecase.business.user.getall.input;

import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.model.vo.pagination.Pagination;
import lombok.Builder;

@Builder
public record GetUsersInput(
    String host, Boolean blockEmail, Pagination pagination, EmailBlockRulesVo blockedRules) {}

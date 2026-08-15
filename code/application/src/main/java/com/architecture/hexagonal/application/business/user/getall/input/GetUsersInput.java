package com.architecture.hexagonal.application.business.user.getall.input;

import com.architecture.hexagonal.domain.model.aggregate.pagination.Pagination;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import lombok.Builder;

@Builder
public record GetUsersInput(
    String host, Boolean blockEmail, Pagination pagination, EmailBlockRulesVo blockedRules) {}

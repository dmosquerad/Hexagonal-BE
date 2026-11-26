package com.architecture.hexagonal.domain.model.criteria.user;

import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import lombok.Builder;

@Builder
public record UserEmailCriteria(String host, Boolean blockEmail, EmailBlockRulesVo blockedRules) {}

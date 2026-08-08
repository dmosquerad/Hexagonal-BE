package com.architecture.hexagonal.application.usecase.business.user.getall.projector;

import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import lombok.Builder;

@Builder
public record UserEmailProjector(String host, Boolean blockEmail, EmailBlockRulesVo blockedRules) {}

package com.architecture.hexagonal.application.projector.business.user;

import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import lombok.Builder;

@Builder
public record UserEmailProjector(String host, Boolean blockEmail, EmailBlockRulesVo blockedRules) {}

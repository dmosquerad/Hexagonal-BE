package com.architecture.hexagonal.domain.model.projector.user;

import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import lombok.Builder;

@Builder
public record UserEmailProjector(String host, Boolean blockEmail, EmailBlockRulesVo blockedRules) {}

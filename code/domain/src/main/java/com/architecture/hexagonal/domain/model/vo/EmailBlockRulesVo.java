package com.architecture.hexagonal.domain.model.vo;

import java.util.Set;
import lombok.Builder;

@Builder
public record EmailBlockRulesVo(
    Set<String> email,
    Set<String> host,
    Set<String> tld,
    Set<String> domain,
    Set<String> username) {}

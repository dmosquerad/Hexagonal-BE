package com.architecture.hexagonal.infrastructure.outbound.configuration.adapter;

import com.architecture.hexagonal.application.port.out.EmailConfigurationPort;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.EmailBlockConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmailConfigurationAdapter implements EmailConfigurationPort {

    private final EmailBlockConfig emailBlockConfig;

    @Override
    public EmailBlockRulesVo getBlockedRules() {
        return EmailBlockRulesVo.builder()
            .email(emailBlockConfig.getEmail())
            .host(emailBlockConfig.getHost())
            .tld(emailBlockConfig.getTld())
            .domain(emailBlockConfig.getDomain())
            .username(emailBlockConfig.getUsername())
            .build();
    }
}


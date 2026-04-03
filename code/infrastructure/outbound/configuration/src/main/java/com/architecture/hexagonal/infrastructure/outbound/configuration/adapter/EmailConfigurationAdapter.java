package com.architecture.hexagonal.infrastructure.outbound.configuration.adapter;

import com.architecture.hexagonal.application.port.out.EmailConfigurationPort;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.domain.model.vo.predicate.EmailVoPredicate;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.EmailBlockConfig;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmailConfigurationAdapter implements EmailConfigurationPort {

    private final EmailBlockConfig emailBlockConfig;

    @Override
    public Set<EmailVo> filterBlocked(final Set<EmailVo> emails) {
        if (emails == null || emails.isEmpty()) {
            return Set.of();
        }
        return emails.stream()
            .filter(this::isBlocked)
            .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<EmailVo> filterAllowed(final Set<EmailVo> emails) {
        if (emails == null || emails.isEmpty()) {
            return Set.of();
        }
        return emails.stream()
            .filter(email -> !isBlocked(email))
            .collect(Collectors.toUnmodifiableSet());
    }

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

    private boolean isBlocked(EmailVo emailVo) {
        if (Objects.isNull(emailVo) 
            || !EmailVoPredicate.CAN_FORM_EMAIL.test(emailVo) 
            || !EmailVoPredicate.CAN_FORM_DOMAIN.test(emailVo)) {
            return false;
        }

        String email = emailVo.getEmail();
        String host = emailVo.getHost();
        String tld = emailVo.getTld();
        String domain = emailVo.getDomain();
        String username = emailVo.getUsername();

        return isBlockedByEmail(email)
                || isBlockedByHost(host)
                || isBlockedByTld(tld)
                || isBlockedByDomain(domain)
                || isBlockedByUsername(username);
    }

    private boolean isBlockedByEmail(String email) {
        return emailBlockConfig.getEmail().stream()
                .anyMatch(blockedEmail -> blockedEmail.equals(email.toLowerCase()));
    }

    private boolean isBlockedByHost(String hostDomain) {
        return emailBlockConfig.getHost().stream()
                .anyMatch(blockedHost -> blockedHost.equals(hostDomain.toLowerCase()));
    }

    private boolean isBlockedByTld(String tld) {
        return emailBlockConfig.getTld().stream()
                .anyMatch(blockedTld -> blockedTld.equals(tld.toLowerCase()));
    }

    private boolean isBlockedByDomain(String hostDomain) {
        return emailBlockConfig.getDomain().stream()
                .anyMatch(blockedDomain -> blockedDomain.equals(hostDomain.toLowerCase()));
    }

    private boolean isBlockedByUsername(String username) {
        return emailBlockConfig.getUsername().stream()
                .anyMatch(blockedUser -> blockedUser.equals(username.toLowerCase()));
    }
}


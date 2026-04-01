package com.architecture.hexagonal.infrastructure.outbound.configuration.adapter;

import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.EmailBlockConfig;
import com.architecture.hexagonal.infrastructure.outbound.configuration.testutils.data.vo.EmailVoTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailConfigurationAdapterTest {

    @InjectMocks
    private EmailConfigurationAdapter emailConfigurationAdapter;

    @Mock
    private EmailBlockConfig emailBlockConfig;

    @Test
    void filterBlocked_shouldReturnEmpty_whenNullEmails() {
        assertThat(emailConfigurationAdapter.filterBlocked(null)).isEmpty();
    }

    @Test
    void filterAllowed_shouldReturnEmpty_whenEmptyEmails() {
        assertThat(emailConfigurationAdapter.filterAllowed(Set.of())).isEmpty();
    }

    @Test
    void filterBlocked_shouldReturnBlockedByHost() {
        EmailVo emailVo = EmailVoTestDataBuilder.builder().build().emailVo();
        when(emailBlockConfig.getUsername()).thenReturn(List.of("test"));

        Set<EmailVo> result = emailConfigurationAdapter.filterBlocked(Set.of(emailVo));

        assertThat(result).containsExactly(emailVo);

        verify(emailBlockConfig).getEmail();
        verify(emailBlockConfig).getUsername();
        verify(emailBlockConfig).getDomain();
        verify(emailBlockConfig).getHost();
        verify(emailBlockConfig).getTld();
    }

    @Test
    void filterAllowed_shouldReturnNotBlocked_whenNoBlockedMatch() {
        EmailVo allowedVo = EmailVoTestDataBuilder.builder().build().emailVo();
        when(emailBlockConfig.getHost()).thenReturn(List.of("test"));

        Set<EmailVo> result = emailConfigurationAdapter.filterAllowed(Set.of(allowedVo));

        assertThat(result).containsExactly(allowedVo);

        verify(emailBlockConfig).getEmail();
        verify(emailBlockConfig).getUsername();
        verify(emailBlockConfig).getDomain();
        verify(emailBlockConfig).getHost();
        verify(emailBlockConfig).getTld();
    }

    @Test
    void filterAllowed_shouldExcludeBlockedHost_whenBlockedMatch() {
        EmailVo blockedVo = EmailVoTestDataBuilder.builder().build().emailVo();
        when(emailBlockConfig.getUsername()).thenReturn(List.of("test"));

        Set<EmailVo> result = emailConfigurationAdapter.filterAllowed(Set.of(blockedVo));

        assertThat(result).isEmpty();

        verify(emailBlockConfig).getEmail();
        verify(emailBlockConfig).getUsername();
        verify(emailBlockConfig).getDomain();
        verify(emailBlockConfig).getHost();
        verify(emailBlockConfig).getTld();
    }
}

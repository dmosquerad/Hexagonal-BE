package com.architecture.hexagonal.infrastructure.outbound.configuration.adapter;

import com.architecture.hexagonal.domain.model.vo.EmailVo;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.TestApplication;
import com.architecture.hexagonal.infrastructure.outbound.configuration.config.EmailBlockConfig;
import com.architecture.hexagonal.infrastructure.outbound.configuration.testutils.data.vo.EmailVoTestDataBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Set;

@SpringBootTest(classes = {EmailConfigurationAdapter.class})
@ContextConfiguration(classes = TestApplication.class)
@ActiveProfiles("configuration")
class EmailConfigurationAdapterTestIT {

    @Autowired
    private EmailConfigurationAdapter emailConfigurationAdapter;

    @MockitoSpyBean
    private EmailBlockConfig emailBlockConfig;

    @Test
    void integrationFilterBlocked_respectsConfiguredBlockedHost() {
        EmailVo blocked = EmailVoTestDataBuilder.builder().build().emailVo();

        Assertions.assertTrue(emailConfigurationAdapter.filterBlocked(Set.of(blocked)).contains(blocked));
    }

    @Test
    void integrationFilterAllowed_willIncludeGenericHostNotConfiguredAsBlocked() {
        EmailVo normal = EmailVoTestDataBuilder.builder().username("pepe").build().emailVo();

        Assertions.assertTrue(emailConfigurationAdapter.filterAllowed(Set.of(normal)).contains(normal));
    }
}

package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import com.architecture.hexagonal.contract.application.usecase.generated.email.GetBlockedRulesQuery;
import com.architecture.hexagonal.contract.inbound.rest.email.server.dto.EmailBlockRulesResponseDto;
import com.architecture.hexagonal.application.port.in.bus.query.QueryBus;
import com.architecture.hexagonal.domain.testutils.data.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.application.testutils.time.TestClock;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.email.EmailBlockRulesDtoMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.resources.email.EmailResponseResource;
import java.time.Clock;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = EmailControllerImpl.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ContextConfiguration(classes = TestApplication.class)
@Import(EmailResponseResource.class)
class EmailControllerImplTestIT {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  JacksonTester<EmailBlockRulesResponseDto> emailBlockRulesResponseDtoJson;

  @Autowired
  EmailResponseResource emailResponseResource;

  @MockitoSpyBean
  EmailControllerImpl emailControllerImpl;

  @MockitoBean
  QueryBus queryBus;

  @MockitoSpyBean
  EmailBlockRulesDtoMapper emailBlockRulesDtoMapper;

  @MockitoSpyBean
  Clock clock;

  @Test
  void getBlockedRulesShouldReturnBlockedRulesResponseWhenBlockedRulesExist() throws Exception {

    final EmailBlockRulesVo emailBlockRulesVo =
        EmailBlockRulesVoTestDataBuilder.builder()
            .email(Set.of("test@example.com"))
            .host(Set.of("example"))
            .tld(Set.of("com"))
            .domain(Set.of("example.com"))
            .username(Set.of("test"))
            .build()
            .emailBlockRulesVo();

    final EmailBlockRulesResponseDto expectedResponse = emailBlockRulesResponseDtoJson
        .readObject(emailResponseResource.getBlockedRules);

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(queryBus.execute(Mockito.any(GetBlockedRulesQuery.class)))
        .thenReturn(emailBlockRulesVo);

    mockMvc.perform(
            MockMvcRequestBuilders.get("/emails/blocks")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content()
                .json(emailBlockRulesResponseDtoJson.write(expectedResponse).getJson()));

    Mockito.verify(emailControllerImpl).getBlockedRules();
    Mockito.verify(queryBus).execute(Mockito.any(GetBlockedRulesQuery.class));
    Mockito.verify(emailBlockRulesDtoMapper).toEmailBlockRulesDto(emailBlockRulesVo);
    Mockito.verify(clock).instant();
  }
}


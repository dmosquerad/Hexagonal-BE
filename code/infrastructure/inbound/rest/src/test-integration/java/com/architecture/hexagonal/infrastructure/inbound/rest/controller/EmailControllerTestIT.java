package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.application.cqrs.query.dispatcher.QueryBus;
import com.architecture.hexagonal.application.cqrs.query.request.GetBlockedRulesQuery;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.contract.rest.email.server.dto.EmailBlockRulesResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.EmailBlockRulesMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.resources.email.EmailResponseResource;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.vo.EmailBlockRulesTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.time.TestClock;
import java.time.Clock;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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

@SpringBootTest(classes = EmailController.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ContextConfiguration(classes = TestApplication.class)
@Import(EmailResponseResource.class)
class EmailControllerTestIT {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  JacksonTester<EmailBlockRulesResponseDto> emailBlockRulesResponseDtoJson;

  @Autowired
  EmailResponseResource emailResponseResource;

  @MockitoSpyBean
  EmailController emailController;

  @MockitoBean
  QueryBus queryBus;

  @MockitoSpyBean
  EmailBlockRulesMapper emailBlockRulesMapper;

  @MockitoSpyBean
  Clock clock;

  @Test
  void getBlockedRules_shouldReturnBlockedRulesResponse_whenBlockedRulesExist() throws Exception {

    final EmailBlockRulesVo emailBlockRulesVo = EmailBlockRulesTestDataBuilder.builder()
        .build()
        .emailBlockRules();

    final EmailBlockRulesResponseDto expectedResponse = emailBlockRulesResponseDtoJson
        .readObject(emailResponseResource.getBlockedRules);

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetBlockedRulesQuery.class)))
        .thenReturn(emailBlockRulesVo);

    mockMvc.perform(
            MockMvcRequestBuilders.get("/emails/blocks")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content()
                .json(emailBlockRulesResponseDtoJson.write(expectedResponse).getJson()));

    Mockito.verify(emailController).getBlockedRules();
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetBlockedRulesQuery.class));
    Mockito.verify(emailBlockRulesMapper).toEmailBlockRulesDto(emailBlockRulesVo);
    Mockito.verify(clock).instant();
  }
}



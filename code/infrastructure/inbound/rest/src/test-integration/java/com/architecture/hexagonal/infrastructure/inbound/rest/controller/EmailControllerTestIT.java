package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.application.cqrs.query.dispatcher.QueryBus;
import com.architecture.hexagonal.application.cqrs.query.request.GetBlockedRulesQuery;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.TestApplication;
import com.architecture.hexagonal.infrastructure.inbound.contract.rest.email.dto.EmailBlockRulesResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.EmailBlockRulesMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.vo.EmailBlockRulesTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.time.TestClock;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Clock;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = EmailController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = TestApplication.class)
class EmailControllerTestIT {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

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

    final EmailBlockRulesResponseDto expectedResponse = objectMapper.readValue(
        new ClassPathResource("getBlockedRules_response.json",
            EmailControllerTestIT.class).getFile(),
        EmailBlockRulesResponseDto.class);

    Mockito.when(clock.instant()).thenReturn(TestClock.FIXED_INSTANT);
    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetBlockedRulesQuery.class)))
        .thenReturn(emailBlockRulesVo);

    final MvcResult result = mockMvc.perform(
            MockMvcRequestBuilders.get("/emails/blocks")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

    final EmailBlockRulesResponseDto actualResponse = objectMapper.readValue(
        result.getResponse().getContentAsString(),
        EmailBlockRulesResponseDto.class);

    AssertionsForClassTypes.assertThat(actualResponse)
        .usingRecursiveComparison()
        .isEqualTo(expectedResponse);

    Mockito.verify(emailController).getBlockedRules();
    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetBlockedRulesQuery.class));
    Mockito.verify(emailBlockRulesMapper).toEmailBlockRulesDto(emailBlockRulesVo);
    Mockito.verify(clock).instant();
  }
}


package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.application.feature.email.getblockedrules.query.GetBlockedRulesQuery;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.contract.rest.email.server.dto.EmailBlockRulesDto;
import com.architecture.hexagonal.infrastructure.contract.rest.email.server.dto.EmailBlockRulesResponseDto;
import com.architecture.hexagonal.infrastructure.cqrs.bus.query.QueryBus;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.EmailBlockRulesMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.EmailBlockRulesDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.dto.EmailBlockRulesResponseDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.data.vo.EmailBlockRulesTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.time.TestClock;
import java.time.Clock;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class EmailControllerTest {

  @InjectMocks
  EmailController emailController;

  @Mock
  QueryBus queryBus;

  @Mock
  EmailBlockRulesMapper emailBlockRulesMapper;

  @Spy
  Clock clock = TestClock.FIXED_CLOCK;

  @Test
  void getBlockedRules_shouldReturnOkBlockRulesResponse_whenBlockedRulesExist() throws Exception {
    final EmailBlockRulesVo emailBlockRulesVo = EmailBlockRulesTestDataBuilder.builder()
        .build()
        .emailBlockRules();

    final EmailBlockRulesDto expectedData = EmailBlockRulesDtoTestDataBuilder.builder()
        .build()
        .emailBlockRulesDto();

    final ResponseEntity<EmailBlockRulesResponseDto> expectedResponse = ResponseEntity.ok(
        EmailBlockRulesResponseDtoTestDataBuilder.builder()
            .build()
            .emailBlockRulesResponseDto());

    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetBlockedRulesQuery.class)))
        .thenReturn(emailBlockRulesVo);
    Mockito.when(emailBlockRulesMapper.toEmailBlockRulesDto(emailBlockRulesVo))
        .thenReturn(expectedData);

    final ResponseEntity<EmailBlockRulesResponseDto> actualResponse = 
        emailController.getBlockedRules();

    AssertionsForClassTypes.assertThat(actualResponse)
        .usingRecursiveComparison()
        .isEqualTo(expectedResponse);

    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetBlockedRulesQuery.class));
    Mockito.verify(emailBlockRulesMapper).toEmailBlockRulesDto(emailBlockRulesVo);
    Mockito.verify(clock).instant();
  }
}

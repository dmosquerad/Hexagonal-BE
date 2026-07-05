package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.email.GetBlockedRulesQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.rest.email.server.dto.EmailBlockRulesDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.rest.email.server.dto.EmailBlockRulesResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query.QueryBus;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.email.EmailBlockRulesDtoMapper;
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
class EmailControllerImplTest {

  @InjectMocks EmailControllerImpl emailControllerImpl;

  @Mock QueryBus queryBus;

  @Mock EmailBlockRulesDtoMapper emailBlockRulesDtoMapper;

  @Spy Clock clock = TestClock.FIXED_CLOCK;

  @Test
  void getBlockedRules_shouldReturnOkBlockRulesResponse_whenBlockedRulesExist() {
    final EmailBlockRulesVo emailBlockRulesVo =
        EmailBlockRulesTestDataBuilder.builder().build().emailBlockRules();

    final EmailBlockRulesDto expectedData =
        EmailBlockRulesDtoTestDataBuilder.builder().build().emailBlockRulesDto();

    final ResponseEntity<EmailBlockRulesResponseDto> expectedResponse =
        ResponseEntity.ok(
            EmailBlockRulesResponseDtoTestDataBuilder.builder()
                .build()
                .emailBlockRulesResponseDto());

    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetBlockedRulesQueryDto.class)))
        .thenReturn(emailBlockRulesVo);
    Mockito.when(emailBlockRulesDtoMapper.toEmailBlockRulesDto(emailBlockRulesVo))
        .thenReturn(expectedData);

    final ResponseEntity<EmailBlockRulesResponseDto> actualResponse =
        emailControllerImpl.getBlockedRules();

    AssertionsForClassTypes.assertThat(actualResponse)
        .usingRecursiveComparison()
        .isEqualTo(expectedResponse);

    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetBlockedRulesQueryDto.class));
    Mockito.verify(emailBlockRulesDtoMapper).toEmailBlockRulesDto(emailBlockRulesVo);
    Mockito.verify(clock).instant();
  }

  @Test
  void getBlockedRules_shouldPropagateDomainException_whenDomainExceptionOccurs() {
    final String errorMessage = "domain exception";

    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetBlockedRulesQueryDto.class)))
        .thenThrow(new DomainException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(() -> emailControllerImpl.getBlockedRules())
        .isInstanceOf(DomainException.class)
        .hasMessage(errorMessage);

    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetBlockedRulesQueryDto.class));
  }

  @Test
  void getBlockedRules_shouldPropagateRuntimeException_whenUnexpectedExceptionOccurs() {
    final String errorMessage = "Unexpected error";

    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetBlockedRulesQueryDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(() -> emailControllerImpl.getBlockedRules())
        .isInstanceOf(RuntimeException.class)
        .hasMessage(errorMessage);

    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetBlockedRulesQueryDto.class));
  }
}

package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.application.testutils.time.TestClock;
import com.architecture.hexagonal.contract.bus.model.email.query.GetBlockedRulesQuery;
import com.architecture.hexagonal.contract.inbound.rest.email.server.dto.EmailBlockRulesDto;
import com.architecture.hexagonal.contract.inbound.rest.email.server.dto.EmailBlockRulesResponseDto;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.testutils.data.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.bus.query.QueryBus;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.email.EmailBlockRulesDtoMapper;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.dto.EmailBlockRulesDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.rest.testutils.dto.EmailBlockRulesResponseDtoTestDataBuilder;
import java.time.Clock;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
  void getBlockedRulesShouldReturnOkBlockRulesResponseWhenBlockedRulesExist() {
    final EmailBlockRulesVo emailBlockRulesVo =
        EmailBlockRulesVoTestDataBuilder.builder().build().emailBlockRulesVo();

    final EmailBlockRulesDto expectedData =
        EmailBlockRulesDtoTestDataBuilder.builder().build().emailBlockRulesDto();

    final ResponseEntity<EmailBlockRulesResponseDto> expectedResponse =
        ResponseEntity.ok(
            EmailBlockRulesResponseDtoTestDataBuilder.builder()
                .build()
                .emailBlockRulesResponseDto());

    Mockito.when(queryBus.execute(Mockito.any(GetBlockedRulesQuery.class)))
        .thenReturn(emailBlockRulesVo);
    Mockito.when(emailBlockRulesDtoMapper.toEmailBlockRulesDto(emailBlockRulesVo))
        .thenReturn(expectedData);

    final ResponseEntity<EmailBlockRulesResponseDto> actualResponse =
        emailControllerImpl.getBlockedRules();

    AssertionsForClassTypes.assertThat(actualResponse)
        .usingRecursiveComparison()
        .isEqualTo(expectedResponse);

    Mockito.verify(queryBus).execute(Mockito.any(GetBlockedRulesQuery.class));
    Mockito.verify(emailBlockRulesDtoMapper).toEmailBlockRulesDto(emailBlockRulesVo);
    Mockito.verify(clock).instant();
  }

  @Test
  void getBlockedRulesShouldPropagateDomainExceptionWhenDomainExceptionOccurs() {
    final String errorMessage = "domain exception";

    Mockito.when(queryBus.execute(Mockito.any(GetBlockedRulesQuery.class)))
        .thenThrow(new DomainException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(() -> emailControllerImpl.getBlockedRules())
        .isInstanceOf(DomainException.class)
        .hasMessage(errorMessage);

    Mockito.verify(queryBus).execute(Mockito.any(GetBlockedRulesQuery.class));
  }

  @Test
  void getBlockedRulesShouldPropagateRuntimeExceptionWhenUnexpectedExceptionOccurs() {
    final String errorMessage = "Unexpected error";

    Mockito.when(queryBus.execute(Mockito.any(GetBlockedRulesQuery.class)))
        .thenThrow(new RuntimeException(errorMessage));

    AssertionsForClassTypes.assertThatThrownBy(() -> emailControllerImpl.getBlockedRules())
        .isInstanceOf(RuntimeException.class)
        .hasMessage(errorMessage);

    Mockito.verify(queryBus).execute(Mockito.any(GetBlockedRulesQuery.class));
  }
}

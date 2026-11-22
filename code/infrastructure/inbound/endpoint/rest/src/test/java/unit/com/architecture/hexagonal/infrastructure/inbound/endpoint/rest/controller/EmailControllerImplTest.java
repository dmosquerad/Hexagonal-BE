package com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.controller;

import com.architecture.hexagonal.contract.bus.model.email.query.GetBlockedRulesQuery;
import com.architecture.hexagonal.contract.inbound.endpoint.rest.email.server.dto.EmailBlockRulesResponseDto;
import com.architecture.hexagonal.domain.databuilder.model.vo.email.EmailBlockRulesVoTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.time.TestTime;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import com.architecture.hexagonal.domain.model.vo.exception.FailureType;
import com.architecture.hexagonal.domain.port.in.bus.query.QueryBus;
import com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.databuilder.dto.EmailBlockRulesResponseDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.mapper.email.EmailBlockRulesDtoMapper;
import java.time.Clock;
import java.util.Set;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
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

  @Spy
  EmailBlockRulesDtoMapper emailBlockRulesDtoMapper =
      Mappers.getMapper(EmailBlockRulesDtoMapper.class);

  @Spy Clock clock = TestTime.FIXED_CLOCK;

  @Test
  void getBlockedRulesShouldReturnOkBlockRulesResponseWhenBlockedRulesExist() {
    final EmailBlockRulesVo emailBlockRulesVo =
        EmailBlockRulesVoTestDataBuilder.builder()
            .email(Set.of("test@example.com"))
            .host(Set.of("example"))
            .tld(Set.of("com"))
            .domain(Set.of("example.com"))
            .username(Set.of("test"))
            .build()
            .emailBlockRulesVo();

    final ResponseEntity<EmailBlockRulesResponseDto> expectedResponse =
        ResponseEntity.ok(
            EmailBlockRulesResponseDtoTestDataBuilder.builder()
                .build()
                .emailBlockRulesResponseDto());

    Mockito.when(queryBus.execute(Mockito.any(GetBlockedRulesQuery.class)))
        .thenReturn(emailBlockRulesVo);

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
        .thenThrow(new DomainException(errorMessage, FailureType.NON_RETRYABLE));

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

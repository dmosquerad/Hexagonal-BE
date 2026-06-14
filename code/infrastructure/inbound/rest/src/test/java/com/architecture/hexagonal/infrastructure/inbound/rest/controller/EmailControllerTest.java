package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.contract.cqrs.generated.email.GetBlockedRulesQueryDto;
import com.architecture.hexagonal.infrastructure.contract.rest.email.server.dto.EmailBlockRulesDto;
import com.architecture.hexagonal.infrastructure.contract.rest.email.server.dto.EmailBlockRulesResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.cqrs.dispatcher.query.QueryBus;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;

@ExtendWith(MockitoExtension.class)
class EmailControllerTest {

  @InjectMocks EmailController emailController;

  @Mock QueryBus queryBus;

  @Mock EmailBlockRulesDtoMapper emailBlockRulesDtoMapper;

  @Spy Clock clock = TestClock.FIXED_CLOCK;

  @Test
  void getBlockedRules_shouldReturnOkBlockRulesResponse_whenBlockedRulesExist() throws Exception {
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
        emailController.getBlockedRules();

    AssertionsForClassTypes.assertThat(actualResponse)
        .usingRecursiveComparison()
        .isEqualTo(expectedResponse);

    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetBlockedRulesQueryDto.class));
    Mockito.verify(emailBlockRulesDtoMapper).toEmailBlockRulesDto(emailBlockRulesVo);
    Mockito.verify(clock).instant();
  }

  @Test
  void getBlockedRules_shouldThrowErrorResponseException_whenDomainExceptionOccurs()
      throws Exception {
    final String errorMessage = "domain exception";

    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetBlockedRulesQueryDto.class)))
        .thenThrow(new DomainException(errorMessage));

    final ProblemDetail problemDetail =
        ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.UNPROCESSABLE_ENTITY, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(() -> emailController.getBlockedRules())
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetBlockedRulesQueryDto.class));
  }

  @Test
  void getBlockedRules_shouldThrowErrorResponseException_whenUnexpectedExceptionOccurs()
      throws Exception {
    final String errorMessage = "Unexpected error";

    Mockito.when(queryBus.execute(ArgumentMatchers.any(GetBlockedRulesQueryDto.class)))
        .thenThrow(new RuntimeException(errorMessage));

    final ProblemDetail problemDetail =
        ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    problemDetail.setDetail(errorMessage);
    final ErrorResponseException errorException =
        new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, problemDetail, null);

    AssertionsForClassTypes.assertThatThrownBy(() -> emailController.getBlockedRules())
        .isInstanceOf(ErrorResponseException.class)
        .usingRecursiveComparison()
        .isEqualTo(errorException);

    Mockito.verify(queryBus).execute(ArgumentMatchers.any(GetBlockedRulesQueryDto.class));
  }
}

package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.email.GetBlockedRulesQueryDto;
import com.architecture.hexagonal.infrastructure.contract.rest.email.server.controller.EmailsApi;
import com.architecture.hexagonal.infrastructure.contract.rest.email.server.dto.EmailBlockRulesResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query.QueryBus;
import com.architecture.hexagonal.infrastructure.inbound.rest.factory.ErrorResponseFactory;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.email.EmailBlockRulesDtoMapper;
import java.time.Clock;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailControllerImpl implements EmailsApi {

  private final QueryBus queryBus;

  private final EmailBlockRulesDtoMapper emailBlockRulesDtoMapper;

  private final Clock clock;

  @Override
  public ResponseEntity<EmailBlockRulesResponseDto> getBlockedRules() {
    try {
      return ResponseEntity.ok(
          buildEmailBlockRulesResponse(queryBus.execute(new GetBlockedRulesQueryDto())));
    } catch (DomainException e) {
      throw ErrorResponseFactory.of(HttpStatus.UNPROCESSABLE_ENTITY, e);
    } catch (Exception e) {
      throw ErrorResponseFactory.of(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }

  private EmailBlockRulesResponseDto buildEmailBlockRulesResponse(
      final EmailBlockRulesVo emailBlockRulesVo) {
    final EmailBlockRulesResponseDto emailBlockRulesResponseDto = new EmailBlockRulesResponseDto();
    emailBlockRulesResponseDto.setDate(OffsetDateTime.now(clock));
    emailBlockRulesResponseDto.setStatus(HttpStatus.OK.value());
    emailBlockRulesResponseDto.setData(
        emailBlockRulesDtoMapper.toEmailBlockRulesDto(emailBlockRulesVo));
    return emailBlockRulesResponseDto;
  }
}

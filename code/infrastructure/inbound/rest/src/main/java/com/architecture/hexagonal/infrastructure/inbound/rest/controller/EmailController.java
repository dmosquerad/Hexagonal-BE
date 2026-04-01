package com.architecture.hexagonal.infrastructure.inbound.rest.controller;

import com.architecture.hexagonal.application.port.in.GetBlockedRulesUseCasePort;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.EmailBlockRulesResponseDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.EmailBlockRulesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.OffsetDateTime;

@RestController
@RequiredArgsConstructor
public class EmailController implements EmailsApi {

  private final GetBlockedRulesUseCasePort getBlockedRulesUseCasePort;

  private final EmailBlockRulesMapper emailBlockRulesMapper;

  private final Clock clock;

  @Override
  public ResponseEntity<EmailBlockRulesResponseDto> getBlockedRules() {
    return ResponseEntity.ok(
        buildEmailBlockRulesResponse(getBlockedRulesUseCasePort.execute()));
  }

  private EmailBlockRulesResponseDto buildEmailBlockRulesResponse(final EmailBlockRulesVo emailBlockRulesVo) {
    final EmailBlockRulesResponseDto emailBlockRulesResponseDto = new EmailBlockRulesResponseDto();
    emailBlockRulesResponseDto.setDate(OffsetDateTime.now(clock));
    emailBlockRulesResponseDto.setStatus(HttpStatus.OK.value());
    emailBlockRulesResponseDto.setData(emailBlockRulesMapper
            .toEmailBlockRulesDto(emailBlockRulesVo));
    return emailBlockRulesResponseDto;
  }
}

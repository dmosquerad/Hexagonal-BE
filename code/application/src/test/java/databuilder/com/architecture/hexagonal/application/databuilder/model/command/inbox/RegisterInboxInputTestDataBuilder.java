package com.architecture.hexagonal.application.databuilder.model.command.inbox;

import com.architecture.hexagonal.application.databuilder.model.vo.payload.MessageHeaderTestDataBuilder;
import com.architecture.hexagonal.application.databuilder.model.vo.time.DateTestDataBuilder;
import com.architecture.hexagonal.application.model.command.inbox.RegisterInboxCommand;
import com.architecture.hexagonal.application.model.vo.payload.MessageHeader;
import com.architecture.hexagonal.application.model.vo.time.Date;
import com.architecture.hexagonal.domain.databuilder.model.vo.time.DateVoTestDataBuilder;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.time.DateVo;

import java.util.Map;
import lombok.Builder;

@Builder
public class RegisterInboxInputTestDataBuilder {

  @Builder.Default
  private MessageHeader messageHeader =
      MessageHeaderTestDataBuilder.builder().build().messageHeader();

  @Builder.Default private Object data = Map.of("userId", "123");
  @Builder.Default private Object aggregateId = "123";
  @Builder.Default private String aggregateType = AggregateTypeVo.USER.getAggregateType();
  @Builder.Default private String action = ActionType.UserActionType.USER_CREATED.name();
  @Builder.Default private Date createdAt = DateTestDataBuilder.builder().build().date();

  public RegisterInboxCommand registerInboxInput() {
    return RegisterInboxCommand.builder()
        .messageHeader(messageHeader)
        .data(data)
        .aggregateId(aggregateId)
        .aggregateType(aggregateType)
        .action(action)
        .createdAt(createdAt)
        .build();
  }
}

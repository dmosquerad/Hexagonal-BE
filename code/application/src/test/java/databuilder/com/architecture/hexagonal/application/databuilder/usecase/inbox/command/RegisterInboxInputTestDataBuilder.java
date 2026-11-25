package com.architecture.hexagonal.application.databuilder.usecase.inbox.command;

import com.architecture.hexagonal.application.databuilder.usecase.common.DateTestDataBuilder;
import com.architecture.hexagonal.contract.bus.model.common.Date;
import com.architecture.hexagonal.contract.bus.model.inbox.command.MessageHeader;
import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
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
    final RegisterInboxCommand command = new RegisterInboxCommand();
    command.setMessageHeader(messageHeader);
    command.setData(data);
    command.setAggregateId(aggregateId);
    command.setAggregateType(aggregateType);
    command.setAction(action);
    command.setCreatedAt(createdAt);
    return command;
  }
}

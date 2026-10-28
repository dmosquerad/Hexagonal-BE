package com.architecture.hexagonal.application.testutils.usecase.inbox.command;

import com.architecture.hexagonal.contract.bus.model.inbox.command.MessageHeader;
import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.testutils.data.time.TestClock;
import java.time.OffsetDateTime;
import java.util.Map;
import lombok.Builder;

@Builder
public class RegisterInboxInputTestDataBuilder {

  @Builder.Default
  private MessageHeader messageHeader = MessageHeaderTestDataBuilder.builder().build().messageHeader();

  @Builder.Default private Object data = Map.of("userId", "123");
  @Builder.Default private Object aggregateId = "123";
  @Builder.Default private String aggregateType = AggregateTypeVo.USER.getAggregateType();
  @Builder.Default private String action = ActionType.UserActionType.USER_CREATED.name();
  @Builder.Default private OffsetDateTime createdAt = OffsetDateTime.now(TestClock.FIXED_CLOCK);

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

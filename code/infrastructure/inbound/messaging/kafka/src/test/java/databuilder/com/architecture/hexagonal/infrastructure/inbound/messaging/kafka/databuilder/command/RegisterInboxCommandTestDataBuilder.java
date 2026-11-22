package com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.databuilder.command;

import com.architecture.hexagonal.application.databuilder.usecase.common.DateTestDataBuilder;
import com.architecture.hexagonal.contract.bus.model.common.Date;
import com.architecture.hexagonal.contract.bus.model.inbox.command.MessageHeader;
import com.architecture.hexagonal.contract.bus.model.inbox.command.RegisterInboxCommand;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.infrastructure.inbound.messaging.kafka.databuilder.message.UserCreatedDataTestDataBuilder;
import lombok.Builder;

@Builder
public class RegisterInboxCommandTestDataBuilder {

  @Builder.Default private MessageHeader messageHeader = MessageHeaderCommandTestDataBuilder.builder().build().messageHeader();

  @Builder.Default private Object data = UserCreatedDataTestDataBuilder.builder().build().userCreatedData();

  @Builder.Default private Object aggregateId = "test-1";

  @Builder.Default private String aggregateType = AggregateTypeVo.USER.getAggregateType();

  @Builder.Default private String action = ActionType.UserActionType.USER_CREATED.getActionType();

  @Builder.Default private Date createdAt = DateTestDataBuilder.builder().build().date();

  public RegisterInboxCommand registerInboxCommand() {
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

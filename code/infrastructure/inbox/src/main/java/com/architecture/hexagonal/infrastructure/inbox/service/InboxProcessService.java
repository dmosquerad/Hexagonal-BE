package com.architecture.hexagonal.infrastructure.inbox.service;

import com.architecture.hexagonal.application.port.out.inbox.InboxProcess;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserCreatedData;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserDeletedData;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserUpdatedData;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.infrastructure.bus.command.CommandBus;
import com.architecture.hexagonal.infrastructure.inbox.mapper.UserCommandFromInboxMapper;
import com.architecture.hexagonal.infrastructure.inbox.naming.InboxNaming;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tools.jackson.databind.ObjectMapper;

@Service
public class InboxProcessService implements InboxProcess {

  private final CommandBus commandBus;
  private final UserCommandFromInboxMapper userCommandFromInboxMapper;
  private final ObjectMapper objectMapper;

  public InboxProcessService(
      final CommandBus commandBus,
      final UserCommandFromInboxMapper userCommandFromInboxMapper,
      final ObjectMapper objectMapper) {
    this.commandBus = commandBus;
    this.userCommandFromInboxMapper = userCommandFromInboxMapper;
    this.objectMapper = objectMapper;
  }

  @PostConstruct
  void validateAggregateHandlers() {
    final Set<AggregateTypeVo> missingAggregateTypes =
        Arrays.stream(AggregateTypeVo.values())
            .filter(type -> !aggregateHandlers().containsKey(type))
            .collect(Collectors.toSet());

    if (!CollectionUtils.isEmpty(missingAggregateTypes)) {
      throw new IllegalStateException(
          InboxNaming.UNSUPPORTED_AGGREGATE_TYPE + missingAggregateTypes);
    }
  }

  @Override
  public void process(final @NonNull Inbox inbox) {
    final Consumer<Inbox> handler = aggregateHandlers().get(inbox.aggregateType());
    if (handler == null) {
      throw new IllegalArgumentException(
          InboxNaming.UNSUPPORTED_AGGREGATE_TYPE + inbox.aggregateType());
    }
    handler.accept(inbox);
  }

  Map<AggregateTypeVo, Consumer<Inbox>> aggregateHandlers() {
    return Map.of(AggregateTypeVo.USER, this::processUserInbox);
  }

  private void processUserInbox(final @NonNull Inbox inbox) {
    final Consumer<Inbox> handler = userActionHandlers().get(inbox.action());
    if (handler == null) {
      throw new IllegalArgumentException(InboxNaming.UNSUPPORTED_USER_ACTION + inbox.action());
    }
    handler.accept(inbox);
  }

  private Map<String, Consumer<Inbox>> userActionHandlers() {
    return Map.of(
        ActionType.UserActionType.USER_CREATED.getActionType(),
            inbox ->
                commandBus.execute(
                    userCommandFromInboxMapper.toCreateUserCommand(
                        objectMapper.convertValue(inbox.payload().data(), UserCreatedData.class))),
        ActionType.UserActionType.USER_UPDATED.getActionType(),
            inbox ->
                commandBus.execute(
                    userCommandFromInboxMapper.toUpdateUserCommand(
                        objectMapper.convertValue(inbox.payload().data(), UserUpdatedData.class))),
        ActionType.UserActionType.USER_DELETED.getActionType(),
            inbox ->
                commandBus.execute(
                    userCommandFromInboxMapper.toDeleteUserCommand(
                        objectMapper.convertValue(inbox.payload().data(), UserDeletedData.class))));
  }
}

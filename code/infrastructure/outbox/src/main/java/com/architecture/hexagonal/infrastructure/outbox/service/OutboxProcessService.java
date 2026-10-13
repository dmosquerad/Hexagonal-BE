package com.architecture.hexagonal.infrastructure.outbox.service;

import com.architecture.hexagonal.application.port.out.outbox.OutboxProcess;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.outbox.ActionType;
import com.architecture.hexagonal.domain.model.vo.outbox.AggregateTypeVo;
import com.architecture.hexagonal.infrastructure.outbox.mapper.user.UserFromOutboxMapper;
import com.architecture.hexagonal.infrastructure.outbox.naming.OutboxNaming;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class OutboxProcessService implements OutboxProcess {

  private final ApplicationEventPublisher applicationEventPublisher;
  private final UserFromOutboxMapper userFromOutboxMapper;

  @PostConstruct
  void validateAggregateHandlers() {
    final Set<AggregateTypeVo> missingAggregateTypes =
        Arrays.stream(AggregateTypeVo.values())
            .filter(type -> !aggregateHandlers().containsKey(type))
            .collect(Collectors.toSet());

    if (!CollectionUtils.isEmpty(missingAggregateTypes)) {
      throw new IllegalStateException(
          OutboxNaming.UNSUPPORTED_AGGREGATE_TYPE + missingAggregateTypes);
    }
  }

  @Override
  public void process(final @NonNull Outbox outbox) {
    final Consumer<Outbox> handler = aggregateHandlers().get(outbox.aggregateType());
    if (handler == null) {
      throw new IllegalArgumentException(
          OutboxNaming.UNSUPPORTED_AGGREGATE_TYPE + outbox.aggregateType());
    }
    handler.accept(outbox);
  }

  Map<AggregateTypeVo, Consumer<Outbox>> aggregateHandlers() {
    return Map.of(AggregateTypeVo.USER, this::processUserOutbox);
  }

  private void processUserOutbox(final @NonNull Outbox outbox) {
    final Consumer<Outbox> handler = userActionHandlers().get(outbox.action());
    if (handler == null) {
      throw new IllegalArgumentException(OutboxNaming.UNSUPPORTED_USER_ACTION + outbox.action());
    }
    handler.accept(outbox);
  }

  private Map<String, Consumer<Outbox>> userActionHandlers() {
    return Map.of(
        ActionType.UserActionType.USER_CREATED.getActionType(),
            outbox ->
                applicationEventPublisher.publishEvent(
                    userFromOutboxMapper.toUserCreated(outbox.payload())),
        ActionType.UserActionType.USER_UPDATED.getActionType(),
            outbox ->
                applicationEventPublisher.publishEvent(
                    userFromOutboxMapper.toUserUpdated(outbox.payload())),
        ActionType.UserActionType.USER_DELETED.getActionType(),
            outbox ->
                applicationEventPublisher.publishEvent(
                    userFromOutboxMapper.toUserDeleted(outbox.payload())));
  }
}

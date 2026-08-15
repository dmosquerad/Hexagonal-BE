package com.architecture.hexagonal.infrastructure.outbound.message.outbox.adapter;

import com.architecture.hexagonal.application.port.outbox.OutboxProcessPort;
import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.service.OutboxService;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.naming.UserMessageNaming;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OutboxProcessAdapterImpl implements OutboxProcessPort {

  private final OutboxService outboxService;

  @Override
  public void process(final @NonNull Outbox outbox) {
    final Consumer<Outbox> handler = aggregateHandlers().get(outbox.aggregateType());
    if (Objects.isNull(handler)) {
      throw new IllegalArgumentException("Unsupported aggregate type: " + outbox.aggregateType());
    }
    handler.accept(outbox);
  }

  private Map<String, Consumer<Outbox>> aggregateHandlers() {
    return Map.of(UserMessageNaming.AGGREGATE_USER_TYPE, outboxService::process);
  }
}

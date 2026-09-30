package com.architecture.hexagonal.infrastructure.outbound.message.outbox.adapter;

import com.architecture.hexagonal.application.port.outbox.OutboxProcessPort;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.outbox.AggregateTypeVo;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.naming.OutboxNaming;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.service.OutboxService;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
@RequiredArgsConstructor
public class OutboxProcessAdapterImpl implements OutboxProcessPort {

  private final OutboxService outboxService;

  @PostConstruct
  void validateAggregateHandlers() {
    final Set<AggregateTypeVo> missingAggregateTypeVo =
        Arrays.stream(AggregateTypeVo.values())
            .filter(type -> !aggregateHandlers().containsKey(type))
            .collect(Collectors.toSet());

    if (!CollectionUtils.isEmpty(missingAggregateTypeVo)) {
      throw new IllegalStateException(
          OutboxNaming.UNSUPPORTED_AGGREGATE_TYPE + missingAggregateTypeVo);
    }
  }

  @Override
  public void process(final @NonNull Outbox outbox) {
    final Consumer<Outbox> handler = aggregateHandlers().get(outbox.aggregateType());
    handler.accept(outbox);
  }

  public Map<AggregateTypeVo, Consumer<Outbox>> aggregateHandlers() {
    return Map.of(AggregateTypeVo.USER, outboxService::process);
  }
}

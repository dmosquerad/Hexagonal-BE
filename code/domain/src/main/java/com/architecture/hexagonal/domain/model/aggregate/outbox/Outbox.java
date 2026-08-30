package com.architecture.hexagonal.domain.model.aggregate.outbox;

import com.architecture.hexagonal.domain.model.aggregate.AggregateRoot;
import com.architecture.hexagonal.domain.model.vo.OutboxStatusVo;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record Outbox(
    UUID outboxId,
    String aggregateType,
    Object aggregateId,
    String action,
    Object payload,
    OutboxStatusVo status,
    int retryCount,
    OffsetDateTime createdAt,
    OffsetDateTime processedAt)
    implements AggregateRoot<Outbox.OutboxKey> {

  @Override
  public OutboxKey getId() {
    return OutboxKey.builder()
        .aggregateType(aggregateType)
        .aggregateId(aggregateId)
        .action(action)
        .payload(payload)
        .build();
  }

  public OutboxKey getIdWithoutActionAndPayload() {
    return OutboxKey.builder().aggregateType(aggregateType).aggregateId(aggregateId).build();
  }

  @Builder
  public record OutboxKey(
      String aggregateType, Object aggregateId, String action, Object payload) {}
}

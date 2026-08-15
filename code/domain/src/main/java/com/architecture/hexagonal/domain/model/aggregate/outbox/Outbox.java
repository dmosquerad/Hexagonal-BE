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
    String aggregateId,
    String action,
    Object payload,
    OutboxStatusVo status,
    int retryCount,
    OffsetDateTime createdAt,
    OffsetDateTime processedAt)
    implements AggregateRoot<UUID> {

  @Override
  public UUID getId() {
    return outboxId;
  }
}

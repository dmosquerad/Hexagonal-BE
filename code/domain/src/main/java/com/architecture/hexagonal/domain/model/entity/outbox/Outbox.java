package com.architecture.hexagonal.domain.model.entity.outbox;

import com.architecture.hexagonal.domain.model.entity.EntityId;
import com.architecture.hexagonal.domain.model.vo.outbox.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.outbox.MessageHeaderVo;
import com.architecture.hexagonal.domain.model.vo.outbox.OutboxStatusVo;
import com.architecture.hexagonal.domain.model.vo.outbox.PayloadVo;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record Outbox(
    UUID outboxId,
    AggregateTypeVo aggregateType,
    Object aggregateId,
    String action,
    PayloadVo payload,
    OutboxStatusVo status,
    Integer retryCount,
    OffsetDateTime createdAt,
    OffsetDateTime processedAt)
    implements EntityId<Outbox.OutboxKey> {

  @Override
  public OutboxKey getId() {
    return OutboxKey.builder()
        .aggregateType(aggregateType)
        .aggregateId(aggregateId)
        .action(action)
        .messageHeaderVo(payload.messageHeader())
        .build();
  }

  @Builder
  public record OutboxKey(
      AggregateTypeVo aggregateType,
      Object aggregateId,
      String action,
      MessageHeaderVo messageHeaderVo) {}
}

package com.architecture.hexagonal.domain.model.entity.outbox;

import com.architecture.hexagonal.domain.model.entity.EntityId;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.MessageHeaderVo;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.domain.model.vo.time.DateVo;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record Outbox(
    UUID outboxId,
    AggregateTypeVo aggregateType,
    Object aggregateId,
    String action,
    PayloadVo payload,
    StatusVo status,
    Integer retryCount,
    DateVo createdAt,
    DateVo processedAt)
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

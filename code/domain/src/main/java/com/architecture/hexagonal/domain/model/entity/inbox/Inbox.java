package com.architecture.hexagonal.domain.model.entity.inbox;

import com.architecture.hexagonal.domain.model.entity.EntityId;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record Inbox(
    PayloadVo payload,
    AggregateTypeVo aggregateType,
    Object aggregateId,
    String action,
    StatusVo status,
    int retryCount,
    OffsetDateTime createdAt,
    OffsetDateTime processedAt)
    implements EntityId<Inbox.InboxKey> {

  @Override
  public InboxKey getId() {
    return InboxKey.builder()
        .messageId(payload.messageHeader().messageId())
        .messageDate(payload.messageHeader().messageDate())
        .build();
  }

  @Builder
  public record InboxKey(UUID messageId, OffsetDateTime messageDate) {}
}

package com.architecture.hexagonal.domain.model.entity.inbox;

import com.architecture.hexagonal.domain.model.entity.EntityId;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.domain.model.vo.time.DateVo;
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
    DateVo createdAt,
    DateVo processedAt)
    implements EntityId<Inbox.InboxKey> {

  @Override
  public InboxKey getId() {
    return InboxKey.builder()
        .messageId(payload.messageHeader().messageId())
        .messageDate(payload.messageHeader().messageDate())
        .build();
  }

  @Builder
  public record InboxKey(UUID messageId, DateVo messageDate) {}
}

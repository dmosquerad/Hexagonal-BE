package com.architecture.hexagonal.domain.model.entity;

import com.architecture.hexagonal.domain.model.vo.OutboxStatusVo;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class OutboxDo {
  UUID outboxId;
  String aggregateType;
  String aggregateId;
  String action;
  Object payload;
  OutboxStatusVo status;
  int retryCount;
  OffsetDateTime createdAt;
  OffsetDateTime processedAt;
}

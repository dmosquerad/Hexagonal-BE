package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.aggregate;

import com.architecture.hexagonal.domain.model.vo.outbox.PayloadVo;
import com.architecture.hexagonal.domain.model.vo.outbox.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.outbox.OutboxStatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.time.TestClock;

import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public class OutboxTestDataBuilder {

  @Builder.Default
  private UUID eventId = UUID.fromString("ec9633ff-1137-4f3a-87ef-ea7738ad411f");

  @Builder.Default
  private AggregateTypeVo aggregateType = AggregateTypeVo.USER;

  @Builder.Default
  private String aggregateId = "123";

  @Builder.Default
  private String action = "USER_CREATED";

  @Builder.Default
  private PayloadVo payload = PayloadTestDataBuilder.builder().build().payload();

  @Builder.Default
  private OutboxStatusVo status = OutboxStatusVo.PENDING;

  @Builder.Default
  private int retryCount = 0;

  @Builder.Default
  private OffsetDateTime createdAt = OffsetDateTime.now(TestClock.FIXED_CLOCK);

  @Builder.Default
  private OffsetDateTime processedAt = OffsetDateTime.now(TestClock.FIXED_CLOCK);

  public Outbox outboxEventDo() {
    return Outbox.builder()
        .outboxId(eventId)
        .aggregateType(aggregateType)
        .aggregateId(aggregateId)
        .action(action)
        .payload(payload)
        .status(status)
        .retryCount(retryCount)
        .createdAt(createdAt)
        .processedAt(processedAt)
        .build();
  }
}

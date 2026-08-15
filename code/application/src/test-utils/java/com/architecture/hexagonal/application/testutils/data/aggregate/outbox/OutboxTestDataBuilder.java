package com.architecture.hexagonal.application.testutils.data.aggregate.outbox;

import com.architecture.hexagonal.application.testutils.time.TestClock;
import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.OutboxStatusVo;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public class OutboxTestDataBuilder {

  @Builder.Default private UUID eventId = UUID.fromString("ec9633ff-1137-4f3a-87ef-ea7738ad411f");

  @Builder.Default private String aggregateType = "USER";

  @Builder.Default private String aggregateId = "123";

  @Builder.Default private String action = "USER_CREATED";

  @Builder.Default private String payload = "{\"id\":\"123\"}";

  @Builder.Default private OutboxStatusVo status = OutboxStatusVo.PENDING;

  @Builder.Default private int retryCount = 0;

  @Builder.Default
  private OffsetDateTime createdAt = OffsetDateTime.now(TestClock.FIXED_CLOCK);

  @Builder.Default
  private OffsetDateTime processedAt = OffsetDateTime.now(TestClock.FIXED_CLOCK);

  public Outbox outboxDo() {
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

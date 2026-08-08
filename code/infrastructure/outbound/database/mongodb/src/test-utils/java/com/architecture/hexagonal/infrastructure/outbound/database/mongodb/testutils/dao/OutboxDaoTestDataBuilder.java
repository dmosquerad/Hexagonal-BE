package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.dao;

import com.architecture.hexagonal.domain.model.vo.outbox.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.outbox.OutboxStatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.PayloadDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.time.TestClock;
import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public class OutboxDaoTestDataBuilder {

  @Builder.Default
  private UUID eventId = UUID.fromString("ec9633ff-1137-4f3a-87ef-ea7738ad411f");

  @Builder.Default
  private AggregateTypeVo aggregateType = AggregateTypeVo.USER;

  @Builder.Default
  private String aggregateId = "123";

  @Builder.Default
  private String action = "USER_CREATED";

  @Builder.Default
  private PayloadDao payload = PayloadDaoTestDataBuilder.builder().build().payloadDao();

  @Builder.Default
  private OutboxStatusVo status = OutboxStatusVo.PENDING;

  @Builder.Default
  private int retryCount = 0;

  @Builder.Default
  private OffsetDateTime createdAt = OffsetDateTime.now(TestClock.FIXED_CLOCK);

  @Builder.Default
  private OffsetDateTime processedAt = OffsetDateTime.now(TestClock.FIXED_CLOCK);

  public OutboxDao outboxEventDao() {
    final OutboxDao outboxDao = new OutboxDao();
    outboxDao.setOutboxId(eventId);
    outboxDao.setAggregateType(aggregateType);
    outboxDao.setAggregateId(aggregateId);
    outboxDao.setAction(action);
    outboxDao.setPayload(payload);
    outboxDao.setStatus(status);
    outboxDao.setRetryCount(retryCount);
    outboxDao.setCreatedAt(createdAt);
    outboxDao.setProcessedAt(processedAt);
    return outboxDao;
  }
}

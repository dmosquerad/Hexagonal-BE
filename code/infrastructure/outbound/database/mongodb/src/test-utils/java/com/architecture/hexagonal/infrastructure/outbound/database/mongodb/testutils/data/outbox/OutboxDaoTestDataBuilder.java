package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.outbox;

import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.outbox.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.PayloadDao;
import com.architecture.hexagonal.domain.testutils.data.time.TestClock;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.payload.PayloadDaoTestDataBuilder;
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
  private String action = ActionType.UserActionType.USER_CREATED.getActionType();

  @Builder.Default
  private PayloadDao payload = PayloadDaoTestDataBuilder.builder().build().payloadDao();

  @Builder.Default
  private StatusVo status = StatusVo.PENDING;

  @Builder.Default
  private int retryCount = 0;

  @Builder.Default
  private ZonedDateTime createdAt = TestClock.FIXED_INSTANT.atZone(ZoneOffset.UTC);

  @Builder.Default
  private ZonedDateTime processedAt = TestClock.FIXED_INSTANT.atZone(ZoneOffset.UTC);

  public OutboxDao outboxDao() {
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

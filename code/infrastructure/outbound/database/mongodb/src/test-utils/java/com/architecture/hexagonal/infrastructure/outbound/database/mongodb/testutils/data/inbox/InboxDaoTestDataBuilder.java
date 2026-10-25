package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.inbox;

import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.domain.testutils.data.time.TestClock;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.PayloadDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.testutils.data.payload.PayloadDaoTestDataBuilder;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public class InboxDaoTestDataBuilder {

  private UUID inboxId;

  @Builder.Default private AggregateTypeVo aggregateType = AggregateTypeVo.USER;

  @Builder.Default private String aggregateId = "123";

  @Builder.Default private String action = ActionType.UserActionType.USER_DELETED.name();

  @Builder.Default
  private PayloadDao payload = PayloadDaoTestDataBuilder.builder().data(null).build().payloadDao();

  @Builder.Default private StatusVo status = StatusVo.PENDING;

  @Builder.Default private int retryCount = 0;

  @Builder.Default
  private ZonedDateTime createdAt = TestClock.FIXED_INSTANT.atZone(ZoneOffset.UTC);

  @Builder.Default
  private ZonedDateTime processedAt = TestClock.FIXED_INSTANT.atZone(ZoneOffset.UTC);

  public InboxDao inboxDao() {
    final InboxDao inboxDao = new InboxDao();
    inboxDao.setInboxId(inboxId);
    inboxDao.setAggregateType(aggregateType);
    inboxDao.setAggregateId(aggregateId);
    inboxDao.setAction(action);
    inboxDao.setPayload(payload);
    inboxDao.setStatus(status);
    inboxDao.setRetryCount(retryCount);
    inboxDao.setCreatedAt(createdAt);
    inboxDao.setProcessedAt(processedAt);
    return inboxDao;
  }
}

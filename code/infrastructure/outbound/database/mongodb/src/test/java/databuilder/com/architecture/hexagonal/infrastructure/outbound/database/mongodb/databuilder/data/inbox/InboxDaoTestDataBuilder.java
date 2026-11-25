package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.inbox;

import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.PayloadDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.time.DateDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.payload.PayloadDaoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.databuilder.data.time.DateDaoTestDataBuilder;
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
  private DateDao createdAt = DateDaoTestDataBuilder.builder().build().dateDao();

  @Builder.Default
  private DateDao processedAt = DateDaoTestDataBuilder.builder().build().dateDao();

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

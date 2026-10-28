package com.architecture.hexagonal.domain.testutils.data.model.entity.inbox;

import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import java.time.OffsetDateTime;
import java.util.Map;

import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.domain.testutils.data.model.vo.payload.PayloadVoTestDataBuilder;
import com.architecture.hexagonal.domain.testutils.data.time.TestClock;
import lombok.Builder;

@Builder
public class InboxTestDataBuilder<T> {

  @Builder.Default private Object data = Map.of("userId", "123");
  @Builder.Default private AggregateTypeVo aggregateType = AggregateTypeVo.USER;
  @Builder.Default private Object aggregateId = "123";
  @Builder.Default private String action = ActionType.UserActionType.USER_DELETED.name();
  @Builder.Default private PayloadVo payload = PayloadVoTestDataBuilder.<T>builder().build().payloadVo();
  @Builder.Default private StatusVo status = StatusVo.PENDING;
  @Builder.Default private int retryCount = 0;
  @Builder.Default private OffsetDateTime createdAt = OffsetDateTime.now(TestClock.FIXED_CLOCK);
  @Builder.Default private OffsetDateTime processedAt =  OffsetDateTime.now(TestClock.FIXED_CLOCK);

  public Inbox inbox() {
    return Inbox.builder()
        .payload(payload)
        .aggregateType(aggregateType)
        .aggregateId(aggregateId)
        .action(action)
        .status(status)
        .retryCount(retryCount)
        .createdAt(createdAt)
        .processedAt(processedAt)
        .build();
  }
}

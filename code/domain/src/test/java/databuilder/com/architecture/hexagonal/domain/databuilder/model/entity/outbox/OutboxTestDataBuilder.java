package com.architecture.hexagonal.domain.databuilder.model.entity.outbox;

import com.architecture.hexagonal.domain.databuilder.model.vo.date.DateVoTestDataBuilder;
import com.architecture.hexagonal.domain.databuilder.model.vo.payload.PayloadVoTestDataBuilder;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.date.DateVo;
import com.architecture.hexagonal.domain.model.vo.payload.ActionType;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import java.util.UUID;
import lombok.Builder;

@Builder
public class OutboxTestDataBuilder<T> {

  @Builder.Default private UUID eventId = UUID.fromString("ec9633ff-1137-4f3a-87ef-ea7738ad411f");
  @Builder.Default private AggregateTypeVo aggregateType = AggregateTypeVo.USER;
  @Builder.Default private String aggregateId = "123";
  @Builder.Default private String action = ActionType.UserActionType.USER_DELETED.name();

  @Builder.Default
  private PayloadVo payload = PayloadVoTestDataBuilder.<T>builder().build().payloadVo();

  @Builder.Default private StatusVo status = StatusVo.PENDING;
  @Builder.Default private int retryCount = 0;

  @Builder.Default private DateVo createdAt = DateVoTestDataBuilder.builder().build().dateVo();

  @Builder.Default private DateVo processedAt = DateVoTestDataBuilder.builder().build().dateVo();

  public Outbox outbox() {
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

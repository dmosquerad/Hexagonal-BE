package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data;

import com.architecture.hexagonal.domain.model.vo.OutboxStatusVo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "outbox")
@FieldNameConstants
public class OutboxDao {

  @Id private UUID outboxId;

  @NotBlank private String aggregateType;

  @Indexed private String aggregateId;

  @NotBlank private String action;

  @NotNull private Object payload;

  @NotEmpty @Indexed private OutboxStatusVo status;

  @PositiveOrZero private int retryCount;

  @CreatedDate private OffsetDateTime createdAt;

  private OffsetDateTime processedAt;
}

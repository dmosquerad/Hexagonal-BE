package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.outbox;

import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload.PayloadDao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "outbox")
@CompoundIndex(
    name = "outbox_message_identity_unique",
    def = "{'payload.messageHeader._id': 1, 'payload.messageHeader.messageDate': 1}",
    unique = true)
@FieldNameConstants
public class OutboxDao {

  @Id private UUID outboxId;

  @NotNull private AggregateTypeVo aggregateType;

  @Indexed private Object aggregateId;

  @NotBlank private String action;

  @NotNull private PayloadDao payload;

  @NotEmpty @Indexed private StatusVo status;

  @PositiveOrZero private int retryCount;

  @CreatedDate private ZonedDateTime createdAt;

  private ZonedDateTime processedAt;
}

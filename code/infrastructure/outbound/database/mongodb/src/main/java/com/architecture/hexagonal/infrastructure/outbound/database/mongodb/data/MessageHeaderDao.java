package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class MessageHeaderDao {

  @NotNull private UUID messageId;
  @NotNull private OffsetDateTime date;
}

package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
public class PayloadDao {

  @NotNull MessageHeaderDao messageHeader;
  @NotNull Object data;
}

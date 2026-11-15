package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
public class DateVoDao {
  @NotNull private Instant date;
  @NotBlank private String zoneId;
}

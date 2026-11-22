package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.time;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.ZoneId;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@Builder
@FieldNameConstants
public class DateDao {

  @NotNull private Instant date;

  @NotNull private ZoneId zone;
}

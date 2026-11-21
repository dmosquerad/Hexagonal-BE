package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.payload;

import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.date.DateDao;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;

@Data
@FieldNameConstants
public class MessageHeaderDao {
  @Id private UUID messageId;
  @NotNull private DateDao messageDate;
}

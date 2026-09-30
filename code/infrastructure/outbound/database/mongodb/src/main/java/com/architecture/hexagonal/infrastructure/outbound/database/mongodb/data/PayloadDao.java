package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data;

import com.architecture.hexagonal.domain.model.vo.outbox.MessageHeaderVo;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
public class PayloadDao {

  @NotNull MessageHeaderVo messageHeader;
  @NotNull Object data;
}

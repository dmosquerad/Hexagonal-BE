package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class MessageHeader {
  UUID messageId;
  OffsetDateTime messageDate;
}

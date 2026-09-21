package com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data;

import java.util.UUID;
import lombok.Data;

@Data
public class UserDeletedMessage {

  UUID userId;
}

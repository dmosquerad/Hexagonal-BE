package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data;

import java.util.UUID;
import lombok.Data;

@Data
public class User {
  UUID userId;
  String name;
  String email;
}

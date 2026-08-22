package com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data;

import lombok.Data;

@Data
public class UserCreatedMessage {

  String name;
  String email;
}

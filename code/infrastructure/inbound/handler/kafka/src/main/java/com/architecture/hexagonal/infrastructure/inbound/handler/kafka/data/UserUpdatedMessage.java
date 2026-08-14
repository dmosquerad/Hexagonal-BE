package com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data;

import lombok.Data;

@Data
public class UserUpdatedMessage {

  String userId;
  String name;
  String email;
}

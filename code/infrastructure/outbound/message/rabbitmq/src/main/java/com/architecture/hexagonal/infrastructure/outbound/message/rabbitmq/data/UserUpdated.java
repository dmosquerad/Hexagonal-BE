package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data;

import lombok.Data;

@Data
public class UserUpdated {

  String userId;
  String name;
  String email;
}

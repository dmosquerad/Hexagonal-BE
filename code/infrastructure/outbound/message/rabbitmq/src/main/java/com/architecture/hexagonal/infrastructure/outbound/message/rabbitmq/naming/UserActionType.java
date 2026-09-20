package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.naming;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserActionType {
  USER_CREATED("USER_CREATED"),
  USER_UPDATED("USER_UPDATED"),
  USER_DELETED("USER_DELETED");

  private final String actionType;
}

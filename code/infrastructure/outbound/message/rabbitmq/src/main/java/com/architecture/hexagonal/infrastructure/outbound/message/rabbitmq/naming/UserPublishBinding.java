package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.naming;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserPublishBinding {
  USER_CREATED("publishUserCreated-out-0"),
  USER_UPDATED("publishUserUpdated-out-0"),
  USER_DELETED("publishUserDeleted-out-0");

  private final String publishBinding;
}

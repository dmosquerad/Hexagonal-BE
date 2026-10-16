package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.naming;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserEventsProducerBinding {
  PUBLISH_USER_CREATED_OUT_0("publish-user-created-out-0"),
  PUBLISH_USER_UPDATED_OUT_0("publish-user-updated-out-0"),
  PUBLISH_USER_DELETED_OUT_0("publish-user-deleted-out-0");

  private final String bindingName;
}

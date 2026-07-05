package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.naming;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMessageNaming {

  public static final String AGGREGATE_USER_TYPE = "USER";
  public static final String ACTION_USER_CREATED = "USER_CREATED";
  public static final String ACTION_USER_UPDATED = "USER_UPDATED";
  public static final String ACTION_USER_DELETED = "USER_DELETED";
  public static final String PUBLISH_USER_CREATED_OUT_BINDING = "publishUserCreated-out-0";
  public static final String PUBLISH_USER_UPDATED_OUT_BINDING = "publishUserUpdated-out-0";
  public static final String PUBLISH_USER_DELETED_OUT_BINDING = "publishUserDeleted-out-0";
}

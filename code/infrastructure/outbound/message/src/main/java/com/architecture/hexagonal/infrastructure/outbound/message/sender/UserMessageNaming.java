package com.architecture.hexagonal.infrastructure.outbound.message.sender;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMessageNaming {

  public static final String PUBLISH_USER_CREATED_OUT_BINDING = "publishUserCreated-out-0";
  public static final String PUBLISH_USER_UPDATED_OUT_BINDING = "publishUserUpdated-out-0";
  public static final String PUBLISH_USER_DELETED_OUT_BINDING = "publishUserDeleted-out-0";
}

package com.architecture.hexagonal.contract.inbound.messaging.kafka.naming.router;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserMessageConsumerBinding {
  USER_CREATE_EVENT(Naming.USER_CREATE_ACTION),
  USER_UPDATE_EVENT(Naming.USER_UPDATE_ACTION),
  USER_DELETE_EVENT(Naming.USER_DELETE_ACTION);

  private final String bindingName;

  @NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
  public static final class Naming {
    public static final String USER_CREATE_ACTION = "UserCreate";
    public static final String USER_UPDATE_ACTION = "UserUpdate";
    public static final String USER_DELETE_ACTION = "UserDelete";
  }
}

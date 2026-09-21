package com.architecture.hexagonal.domain.model.vo.outbox;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class ActionType {

  @Getter
  @RequiredArgsConstructor
  public enum UserActionType {
    USER_CREATED("USER_CREATED"),
    USER_UPDATED("USER_UPDATED"),
    USER_DELETED("USER_DELETED");

    private final String actionType;
  }
}

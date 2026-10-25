package com.architecture.hexagonal.domain.model.vo.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class ActionType {

  @Getter
  @RequiredArgsConstructor
  public enum UserActionType {
    USER_CREATED(Naming.USER_CREATED_ACTION),
    USER_UPDATED(Naming.USER_UPDATED_ACTION),
    USER_DELETED(Naming.USER_DELETED_ACTION);

    private final String actionType;

    @NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
    public static final class Naming {
      public static final String USER_CREATED_ACTION = "USER_CREATED";
      public static final String USER_UPDATED_ACTION = "USER_UPDATED";
      public static final String USER_DELETED_ACTION = "USER_DELETED";
    }
  }
}

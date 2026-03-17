package com.architecture.hexagonal.domain.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessage {
  public static final String NOT_FOUND_DATA_MESSAGE = "Not found data by uuid: ";
}

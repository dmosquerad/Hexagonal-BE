package com.architecture.hexagonal.infrastructure.outbound.message.outbox.naming;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OutboxNaming {

  public static final String UNSUPPORTED_AGGREGATE_TYPE = "Unsupported aggregate type: ";
  public static final String UNSUPPORTED_USER_ACTION = "Unsupported user action: ";
}

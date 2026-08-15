package com.architecture.hexagonal.infrastructure.outbound.message.outbox.naming;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OutboxNaming {

  public static final String UNSUPPORTED_AGGREGATE_TYPE = "Unsupported aggregate type: ";
  public static final String UNSUPPORTED_USER_ACTION = "Unsupported user action: ";
}

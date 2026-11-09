package com.architecture.hexagonal.contract.inbound.messaging.kafka.naming.header;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class HeaderNaming {

  public static final String EVENT_TYPE_HEADER = "X-Event-Type";
  public static final String EVENT_ID_HEADER = "X-Event-Id";
  public static final String SCHEMA_VERSION_HEADER = "X-Schema-Version";
}

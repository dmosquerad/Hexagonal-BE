package com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query;

import lombok.NonNull;

public interface QueryBus {
  <Q, R> R execute(@NonNull Q query);
}

package com.architecture.hexagonal.infrastructure.bus.query;

import lombok.NonNull;

public interface QueryBus {

  <Q, R> R execute(@NonNull Q query);
}

package com.architecture.hexagonal.domain.port.in.bus.query;

import lombok.NonNull;

public interface QueryBus {

  <Q, R> R execute(@NonNull Q query);
}

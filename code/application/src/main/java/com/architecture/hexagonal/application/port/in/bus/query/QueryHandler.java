package com.architecture.hexagonal.application.port.in.bus.query;

import lombok.NonNull;

public interface QueryHandler<Q, R> {

  R handle(@NonNull Q query);
}

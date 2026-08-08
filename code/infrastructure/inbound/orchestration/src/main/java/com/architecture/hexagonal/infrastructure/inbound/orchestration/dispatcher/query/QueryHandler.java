package com.architecture.hexagonal.infrastructure.inbound.orchestration.dispatcher.query;

import lombok.NonNull;

public interface QueryHandler<Q, R> {

  R handle(@NonNull Q query);
}

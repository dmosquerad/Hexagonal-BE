package com.architecture.hexagonal.domain.port.out.bus.event;

import lombok.NonNull;

public interface EventPublisher<E> {

  void publish(@NonNull E event);
}

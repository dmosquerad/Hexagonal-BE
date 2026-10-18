package com.architecture.hexagonal.application.port.in.bus.event;

import lombok.NonNull;

public interface EventHandler<E> {

  void handle(@NonNull E event);
}

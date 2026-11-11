package com.architecture.hexagonal.domain.port.out.bus.event;

import lombok.NonNull;

public interface EventBus {

  <E> void publish(@NonNull E event);
}

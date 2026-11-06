package com.architecture.hexagonal.application.port.out.bus.event;

import lombok.NonNull;

public interface EventBus {

  <E> void publish(@NonNull E event);
}

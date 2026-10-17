package com.architecture.hexagonal.application.port.in.bus.query;

import com.architecture.hexagonal.application.port.in.bus.MessageHandler;
import lombok.NonNull;

public interface QueryHandler<Q, R> extends MessageHandler {

  R handle(@NonNull Q query);
}

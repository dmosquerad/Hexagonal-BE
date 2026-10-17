package com.architecture.hexagonal.infrastructure.bus;

import com.architecture.hexagonal.application.port.in.bus.MessageHandler;
import com.architecture.hexagonal.application.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.application.port.in.bus.query.QueryHandler;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

@Component
public final class MessageHandlerFinder {

  private final Map<Class<?>, List<MessageHandler>> handlersByMessage;

  public MessageHandlerFinder(final List<MessageHandler> handlers) {
    this.handlersByMessage = buildHandlersByMessage(handlers);
  }

  private Map<Class<?>, List<MessageHandler>> buildHandlersByMessage(
      final List<MessageHandler> handlers) {
    return handlers.stream()
        .collect(
            Collectors.groupingBy(
                this::getMessageType,
                Collectors.collectingAndThen(Collectors.toList(), List::copyOf)));
  }

  private Class<?> getMessageType(final Object handler) {
    final Class<?> handlerClass = AopProxyUtils.ultimateTargetClass(handler);

    final boolean isCommandHandler = CommandHandler.class.isAssignableFrom(handlerClass);
    final boolean isQueryHandler = QueryHandler.class.isAssignableFrom(handlerClass);
    if (isCommandHandler == isQueryHandler) {
      throw new IllegalArgumentException(
          "Expected handler to implement exactly one of CommandHandler or QueryHandler: "
              + handlerClass.getName());
    }

    final Class<?> handlerType = isCommandHandler ? CommandHandler.class : QueryHandler.class;
    final Class<?> messageType =
        ResolvableType.forClass(handlerClass).as(handlerType).getGeneric(0).resolve();

    if (messageType == null) {
      throw new IllegalArgumentException(
          "Could not resolve message type for handler " + handlerClass.getName());
    }
    return messageType;
  }

  public <H extends MessageHandler> H find(final Object message, final Class<H> handlerType) {
    final List<MessageHandler> matchingHandlers =
        handlersByMessage.getOrDefault(message.getClass(), List.of()).stream()
            .filter(handlerType::isInstance)
            .toList();
    if (matchingHandlers.size() != 1) {
      throw new IllegalStateException(
          "Expected one "
              + handlerType.getSimpleName()
              + " for "
              + message.getClass()
              + ", found "
              + matchingHandlers.size());
    }
    return handlerType.cast(matchingHandlers.getFirst());
  }
}

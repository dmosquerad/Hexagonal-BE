package com.architecture.hexagonal.infrastructure.bus;

import com.architecture.hexagonal.application.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.application.port.in.bus.query.QueryHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

@Component
public final class MessageHandlerFinder {

  private final Map<Class<?>, List<Object>> handlersByMessage;

  public MessageHandlerFinder(
      final List<CommandHandler<?, ?>> commandHandlers,
      final List<QueryHandler<?, ?>> queryHandlers) {
    this.handlersByMessage = buildHandlersByMessage(commandHandlers, queryHandlers);
  }

  private Map<Class<?>, List<Object>> buildHandlersByMessage(
      final List<CommandHandler<?, ?>> commandHandlers,
      final List<QueryHandler<?, ?>> queryHandlers) {
    final Map<Class<?>, List<Object>> handlersByMessage = new HashMap<>();
    commandHandlers.forEach(
        handler -> addHandler(handlersByMessage, handler, CommandHandler.class));
    queryHandlers.forEach(handler -> addHandler(handlersByMessage, handler, QueryHandler.class));
    handlersByMessage.replaceAll((messageType, handlers) -> List.copyOf(handlers));
    return Map.copyOf(handlersByMessage);
  }

  private void addHandler(
      final Map<Class<?>, List<Object>> handlersByMessage,
      final Object handler,
      final Class<?> handlerType) {
    final Class<?> messageType = getMessageType(handler, handlerType);
    handlersByMessage.computeIfAbsent(messageType, ignored -> new ArrayList<>()).add(handler);
  }

  private Class<?> getMessageType(final Object handler, final Class<?> handlerType) {
    final Class<?> handlerClass = AopProxyUtils.ultimateTargetClass(handler);
    final Class<?> messageType =
        ResolvableType.forClass(handlerClass).as(handlerType).getGeneric(0).resolve();

    if (messageType == null) {
      throw new IllegalArgumentException(
          "Could not resolve message type for handler " + handlerClass.getName());
    }
    return messageType;
  }

  public <H> H find(final Object message, final Class<H> handlerType) {
    final List<Object> matchingHandlers =
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

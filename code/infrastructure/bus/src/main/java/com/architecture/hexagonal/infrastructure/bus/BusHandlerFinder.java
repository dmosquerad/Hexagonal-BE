package com.architecture.hexagonal.infrastructure.bus;

import com.architecture.hexagonal.application.port.in.bus.command.CommandHandler;
import com.architecture.hexagonal.application.port.in.bus.event.EventHandler;
import com.architecture.hexagonal.application.port.in.bus.query.QueryHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class BusHandlerFinder implements SmartInitializingSingleton {

  private final ListableBeanFactory beanFactory;

  private Map<Class<?>, List<Object>> handlersByMessage = Map.of();

  @Override
  public void afterSingletonsInstantiated() {
    final Map<Class<?>, List<Object>> mutableHandlersByMessage = new HashMap<>();
    beanFactory
        .getBeansOfType(CommandHandler.class)
        .values()
        .forEach(handler -> addHandler(mutableHandlersByMessage, handler, CommandHandler.class));
    beanFactory
        .getBeansOfType(QueryHandler.class)
        .values()
        .forEach(handler -> addHandler(mutableHandlersByMessage, handler, QueryHandler.class));
    beanFactory
        .getBeansOfType(EventHandler.class)
        .values()
        .forEach(handler -> addHandler(mutableHandlersByMessage, handler, EventHandler.class));
    mutableHandlersByMessage.replaceAll((messageType, handlers) -> List.copyOf(handlers));
    handlersByMessage = Map.copyOf(mutableHandlersByMessage);
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

  public <H> List<H> findAll(final Object message, final Class<H> handlerType) {
    return handlersByMessage.getOrDefault(message.getClass(), List.of()).stream()
        .filter(handlerType::isInstance)
        .map(handlerType::cast)
        .toList();
  }

  public <H> H find(final Object message, final Class<H> handlerType) {
    final List<H> matchingHandlers = findAll(message, handlerType);
    if (matchingHandlers.size() != 1) {
      throw new IllegalStateException(
          "Expected one "
              + handlerType.getSimpleName()
              + " for "
              + message.getClass()
              + ", found "
              + matchingHandlers.size());
    }
    return matchingHandlers.getFirst();
  }
}

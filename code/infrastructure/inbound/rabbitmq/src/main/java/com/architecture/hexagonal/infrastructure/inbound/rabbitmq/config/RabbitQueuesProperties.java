package com.architecture.hexagonal.infrastructure.inbound.rabbitmq.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitQueuesProperties {

  private String exchange = "hexagonal";
  private Map<String, String> commandQueues = new HashMap<>();
  private Map<String, String> queryQueues = new HashMap<>();
  private Map<String, String> commandBindings = new HashMap<>();
  private Map<String, String> queryBindings = new HashMap<>();

  public String getExchange() {
    return exchange;
  }

  public void setExchange(final String exchange) {
    this.exchange = exchange;
  }

  public Map<String, String> getCommandQueues() {
    return commandQueues;
  }

  public void setCommandQueues(final Map<String, String> commandQueues) {
    this.commandQueues = commandQueues;
  }

  public Map<String, String> getQueryQueues() {
    return queryQueues;
  }

  public void setQueryQueues(final Map<String, String> queryQueues) {
    this.queryQueues = queryQueues;
  }

  public Map<String, String> getCommandBindings() {
    return commandBindings;
  }

  public void setCommandBindings(final Map<String, String> commandBindings) {
    this.commandBindings = commandBindings;
  }

  public Map<String, String> getQueryBindings() {
    return queryBindings;
  }

  public void setQueryBindings(final Map<String, String> queryBindings) {
    this.queryBindings = queryBindings;
  }

  public String getCommandQueueName(final String commandType, final String defaultQueueName) {
    return commandQueues.getOrDefault(commandType, defaultQueueName);
  }

  public String getQueryQueueName(final String queryType, final String defaultQueueName) {
    return queryQueues.getOrDefault(queryType, defaultQueueName);
  }

  public String getCommandBinding(final String commandType, final String defaultBinding) {
    return commandBindings.getOrDefault(commandType, defaultBinding);
  }

  public String getQueryBinding(final String queryType, final String defaultBinding) {
    return queryBindings.getOrDefault(queryType, defaultBinding);
  }
}

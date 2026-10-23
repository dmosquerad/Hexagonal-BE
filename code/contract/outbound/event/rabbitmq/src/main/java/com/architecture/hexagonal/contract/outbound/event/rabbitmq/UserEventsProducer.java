package com.architecture.hexagonal.contract.outbound.event.rabbitmq;

import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserCreated;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserDeleted;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserUpdated;

public interface UserEventsProducer {

  boolean publishUserCreated(UserCreated payload);

  boolean publishUserUpdated(UserUpdated payload);

  boolean publishUserDeleted(UserDeleted payload);
}

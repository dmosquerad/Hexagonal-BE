package com.architecture.hexagonal.contract.inbound.messaging.kafka;

import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserCreated;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserDeleted;
import com.architecture.hexagonal.contract.inbound.messaging.kafka.model.UserUpdated;

public interface UserEventsConsumerService {

  void onUserCreated(UserCreated payload);

  void onUserUpdated(UserUpdated payload);

  void onUserDeleted(UserDeleted payload);
}

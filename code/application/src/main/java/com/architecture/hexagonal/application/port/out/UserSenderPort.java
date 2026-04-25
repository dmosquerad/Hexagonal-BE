package com.architecture.hexagonal.application.port.out;

import com.architecture.hexagonal.domain.model.entity.User;

public interface UserSenderPort {

  void userSenderCreated(User user);

  void userSenderUpdated(User user);

  void userSenderDeleted(User user);
}

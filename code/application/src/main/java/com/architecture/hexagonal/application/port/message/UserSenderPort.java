package com.architecture.hexagonal.application.port.message;

import com.architecture.hexagonal.domain.model.aggregate.User;
import lombok.NonNull;

public interface UserSenderPort {

  void userSenderCreated(@NonNull User user);

  void userSenderUpdated(@NonNull User user);

  void userSenderDeleted(@NonNull User user);
}

package com.hexagonal.stub;

import com.architecture.hexagonal.domain.data.UserDo;
import java.util.UUID;

public class UserDoStub {

  public static final UserDo USERDO = UserDo.builder()
      .userId(UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4"))
      .email("test@example.com")
      .name("Test User")
      .build();
}

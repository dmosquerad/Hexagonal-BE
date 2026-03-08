package com.hexagonal.outbound.database.testutils.data.dao;

import com.architecture.hexagonal.outbound.database.data.UserDao;
import java.util.UUID;
import lombok.Builder;

@Builder
public class UserDaoTestDataBuilder {

  @Builder.Default
  private UUID userId = UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4");

  @Builder.Default
  private String email = "test@example.com";

  @Builder.Default
  private String name = "Test User";

  public UserDao userDao() {
    final UserDao userDao = new UserDao();
    userDao.setUserId(userId);
    userDao.setEmail(email);
    userDao.setName(name);

    return userDao;
  }
}

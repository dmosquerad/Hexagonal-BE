package com.hexagonal.outbound.memory.stub;

import com.architecture.hexagonal.outbound.memory.data.UserDao;
import java.util.UUID;

public class UserDaoStub {

  public static UserDao userDao() {
    final UserDao userDao = new UserDao();
    userDao.setUserId(UUID.fromString("4059510b-ceb3-4d4c-913e-1759acbd62a4"));
    userDao.setEmail("test@example.com");
    userDao.setName("Test User");

    return userDao;
  }
}
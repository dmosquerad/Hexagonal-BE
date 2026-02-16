package com.architecture.hexagonal.outbound.memory.repository;

import com.architecture.hexagonal.outbound.memory.data.UserDao;

public interface UserMemoryWriteRepository {
  UserDao createUser(UserDao userDao);
}

package com.architecture.hexagonal.outbound.memory.repository.impl;

import com.architecture.hexagonal.outbound.memory.data.UserDao;
import com.architecture.hexagonal.outbound.memory.repository.UserMemoryWriteRepository;
import com.architecture.hexagonal.outbound.memory.storage.UserMemoryStorage;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserMemoryWriteRepositoryImpl implements UserMemoryWriteRepository {

  private final UserMemoryStorage userMemoryStorage;

  @Override
  public UserDao createUser(UserDao userDao) {
    userDao.setUserId(UUID.randomUUID());
    userMemoryStorage.getUsers().add(userDao);

    return userDao;
  }
}

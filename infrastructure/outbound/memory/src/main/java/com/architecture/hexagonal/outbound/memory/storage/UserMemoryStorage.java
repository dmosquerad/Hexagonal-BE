package com.architecture.hexagonal.outbound.memory.storage;

import com.architecture.hexagonal.outbound.memory.data.UserDao;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import org.springframework.stereotype.Component;


@Getter
@Component
public class UserMemoryStorage {

  private Set<UserDao> users;

  @PostConstruct
  public void setUp() {
    this.users = Collections.synchronizedSet(new HashSet<>());
    initializeSampleData();
  }

  private void initializeSampleData() {
    UserDao userDao = new UserDao();
    userDao.setUserId(UUID.randomUUID());
    userDao.setName("Hello");
    userDao.setEmail("World@mail.com");
    users.add(userDao);
  }
}

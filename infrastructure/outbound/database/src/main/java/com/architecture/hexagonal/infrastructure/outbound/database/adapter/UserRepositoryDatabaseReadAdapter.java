package com.architecture.hexagonal.infrastructure.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.UserMapper;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.infrastructure.outbound.database.repository.UserDatabaseReadRepository;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRepositoryDatabaseReadAdapter implements UserRepositoryReadPort {
  private final UserDatabaseReadRepository userDatabaseReadRepository;

  private final UserMapper userMapper;

  @Override
  public Set<User> getAllUsers(){
    return userMapper.toUserSet(userDatabaseReadRepository.findAll());
  }

  @Override
  public User findUserById(UUID uuid) {
    return userMapper.toUser(userDatabaseReadRepository.findByUserId(uuid).orElse(new UserDao()));
  }
}

package com.architecture.hexagonal.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.outbound.database.mapper.UserDaoMapper;
import com.architecture.hexagonal.outbound.database.mapper.UserDoMapper;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import com.architecture.hexagonal.outbound.database.repository.UserDatabaseWriteRepository;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRepositoryDatabaseWriteAdapter implements UserRepositoryWritePort {
  private final UserDatabaseWriteRepository userDatabaseWriteRepository;

  private final UserDoMapper userDoMapper;

  private final UserDaoMapper userDaoMapper;

  @Override
  public UserDo saveUser(UserDo userDo) {
    return userDoMapper.toUserDo(userDatabaseWriteRepository.save(userDaoMapper.toUserDao(userDo)));
  }
}

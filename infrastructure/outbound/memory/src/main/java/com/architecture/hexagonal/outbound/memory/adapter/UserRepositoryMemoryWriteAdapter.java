package com.architecture.hexagonal.outbound.memory.adapter;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.outbound.memory.mapper.UserDaoMapper;
import com.architecture.hexagonal.outbound.memory.mapper.UserDoMapper;
import com.architecture.hexagonal.domain.port.out.UserRepositoryWritePort;
import com.architecture.hexagonal.outbound.memory.repository.UserMemoryWriteRepository;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRepositoryMemoryWriteAdapter implements UserRepositoryWritePort {
  private final UserMemoryWriteRepository userMemoryWriteRepository;

  private final UserDoMapper userDoMapper;

  private final UserDaoMapper userDaoMapper;

  @Override
  public UserDo createUser(UserDo userDo) {
    return userDoMapper.toUserDo(userMemoryWriteRepository.createUser(userDaoMapper.toUserDao(userDo)));
  }
}

package com.architecture.hexagonal.outbound.memory.adapter;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.outbound.memory.data.UserDao;
import com.architecture.hexagonal.outbound.memory.mapper.UserDoMapper;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.outbound.memory.repository.UserMemoryReadRepository;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRepositoryMemoryReadAdapter implements UserRepositoryReadPort {
  private final UserMemoryReadRepository userMemoryReadRepository;

  private final UserDoMapper userDoMapper;

  @Override
  public Set<UserDo> getAllUsers(){
    return userDoMapper.toUserDoSet(userMemoryReadRepository.getAllUsers());
  }

  @Override
  public UserDo findUserById(UUID uuid) {
    return userDoMapper.toUserDo(userMemoryReadRepository.findUserById(uuid).orElse(new UserDao()));
  }
}

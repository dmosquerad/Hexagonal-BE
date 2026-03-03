package com.architecture.hexagonal.outbound.database.adapter;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.outbound.database.data.UserDao;
import com.architecture.hexagonal.outbound.database.mapper.UserDoMapper;
import com.architecture.hexagonal.domain.port.out.UserRepositoryReadPort;
import com.architecture.hexagonal.outbound.database.repository.UserDatabaseReadRepository;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRepositoryDatabaseReadAdapter implements UserRepositoryReadPort {
  private final UserDatabaseReadRepository userDatabaseReadRepository;

  private final UserDoMapper userDoMapper;

  @Override
  public Set<UserDo> getAllUsers(){
    return userDoMapper.toUserDoSet(userDatabaseReadRepository.findAll());
  }

  @Override
  public UserDo findUserById(UUID uuid) {
    return userDoMapper.toUserDo(userDatabaseReadRepository.findByUserId(uuid).orElse(new UserDao()));
  }
}

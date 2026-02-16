package com.architecture.hexagonal.outbound.memory.mapper;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.outbound.memory.data.UserDao;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface UserDoMapper {
  UserDo toUserDo(UserDao userDao);

  Set<UserDo> toUserDoSet(Set<UserDao> userDao);
}

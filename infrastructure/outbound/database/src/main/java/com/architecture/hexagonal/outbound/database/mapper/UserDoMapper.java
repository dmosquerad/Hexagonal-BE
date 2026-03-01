package com.architecture.hexagonal.outbound.database.mapper;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.outbound.database.data.UserDao;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface UserDoMapper {
  UserDo toUserDo(UserDao userDao);

  Set<UserDo> toUserDoSet(List<UserDao> userDao);
}

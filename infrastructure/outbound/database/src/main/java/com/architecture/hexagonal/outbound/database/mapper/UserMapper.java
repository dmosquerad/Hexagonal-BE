package com.architecture.hexagonal.outbound.database.mapper;

import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.outbound.database.data.UserDao;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper
{
  User toUser(UserDao userDao);

  Set<User> toUserSet(List<UserDao> userDao);
}

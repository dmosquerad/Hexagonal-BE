package com.architecture.hexagonal.infrastructure.outbound.database.mapper;

import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
  User toUser(UserDao userDao);
}

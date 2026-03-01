package com.architecture.hexagonal.outbound.database.mapper;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.outbound.database.data.UserDao;
import org.mapstruct.Mapper;

@Mapper
public interface UserDaoMapper {
  UserDao toUserDao(UserDo userDo);
}

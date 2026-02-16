package com.architecture.hexagonal.outbound.memory.mapper;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.outbound.memory.data.UserDao;
import org.mapstruct.Mapper;

@Mapper
public interface UserDaoMapper {
  UserDao toUserDao(UserDo userDo);
}

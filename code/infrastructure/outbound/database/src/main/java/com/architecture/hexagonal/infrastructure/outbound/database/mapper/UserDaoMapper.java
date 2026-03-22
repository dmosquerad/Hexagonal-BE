package com.architecture.hexagonal.infrastructure.outbound.database.mapper;

import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.util.ToEmailUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = ToEmailUtil.class)
public interface UserDaoMapper {
  @Mapping(source = "email", target = "email", qualifiedByName = ToEmailUtil.TO_EMAIL)
  UserDao toUserDao(User user);
}

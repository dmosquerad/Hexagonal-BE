package com.architecture.hexagonal.infrastructure.outbound.database.mapper;

import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.util.ToEmailVoUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = ToEmailVoUtil.class)
public interface UserMapper {
  @Mapping(source = "email", target = "email", qualifiedByName = ToEmailVoUtil.TO_EMAIL_VO)
  User toUser(UserDao userDao);
}

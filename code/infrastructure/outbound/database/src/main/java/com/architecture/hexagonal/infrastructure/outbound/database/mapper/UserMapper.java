package com.architecture.hexagonal.infrastructure.outbound.database.mapper;

import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.infrastructure.outbound.database.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.converter.EmailVoConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = EmailVoConverter.class)
public interface UserMapper {
  @Mapping(source = "email", target = "email", qualifiedByName = EmailVoConverter.TO_EMAIL_VO)
  User toUser(UserDao userDao);
}

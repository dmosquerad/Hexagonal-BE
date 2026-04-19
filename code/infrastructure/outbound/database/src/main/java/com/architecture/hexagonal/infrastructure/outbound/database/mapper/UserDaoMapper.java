package com.architecture.hexagonal.infrastructure.outbound.database.mapper;

import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.infrastructure.outbound.database.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.database.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mapper.converter.EmailConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = EmailConverter.class)
public interface UserDaoMapper {
  @Mapping(source = "email", target = "email", qualifiedByName = EmailConverter.TO_EMAIL)
  UserDao toUserDao(User user);
}

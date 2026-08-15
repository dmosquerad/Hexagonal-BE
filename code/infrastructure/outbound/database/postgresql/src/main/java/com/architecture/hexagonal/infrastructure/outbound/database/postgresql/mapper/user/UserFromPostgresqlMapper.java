package com.architecture.hexagonal.infrastructure.outbound.database.postgresql.mapper.user;

import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.data.UserDao;
import com.architecture.hexagonal.infrastructure.outbound.database.postgresql.mapper.converter.EmailVoConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = EmailVoConverter.class)
public interface UserFromPostgresqlMapper {
  @Mapping(source = "email", target = "email", qualifiedByName = EmailVoConverter.TO_EMAIL_VO)
  User toUser(UserDao userDao);
}

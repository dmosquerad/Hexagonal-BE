package com.architecture.hexagonal.infrastructure.outbound.message.outbox.mapper.user;

import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.config.mapstruct.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.message.outbox.mapper.converter.EmailVoConverter;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserUpdated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = EmailVoConverter.class)
public interface UserFromOutboxMapper {

  @Mapping(source = "userId", target = "user.userId")
  @Mapping(source = "name", target = "user.name")
  @Mapping(source = "email", target = "email", qualifiedByName = EmailVoConverter.TO_EMAIL_VO)
  User toUser(UserCreated userCreated);

  @Mapping(source = "userId", target = "user.userId")
  @Mapping(source = "name", target = "user.name")
  @Mapping(source = "email", target = "email", qualifiedByName = EmailVoConverter.TO_EMAIL_VO)
  User toUser(UserUpdated userUpdated);

  @Mapping(source = "userId", target = "user.userId")
  @Mapping(source = "name", target = "user.name")
  @Mapping(source = "email", target = "email", qualifiedByName = EmailVoConverter.TO_EMAIL_VO)
  User toUser(UserDeleted userDeleted);
}

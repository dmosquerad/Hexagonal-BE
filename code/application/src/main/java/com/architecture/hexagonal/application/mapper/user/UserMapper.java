package com.architecture.hexagonal.application.mapper.user;

import com.architecture.hexagonal.application.mapper.converter.EmailVoConverter;
import com.architecture.hexagonal.application.model.command.user.CreateUserCommand;
import com.architecture.hexagonal.application.model.command.user.PatchUserCommand;
import com.architecture.hexagonal.application.model.command.user.UpdateUserCommand;
import com.architecture.hexagonal.domain.model.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = EmailVoConverter.class)
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(target = "userId", ignore = true)
  @Mapping(source = "email", target = "email", qualifiedByName = EmailVoConverter.TO_EMAIL_VO)
  User toUser(CreateUserCommand command);

  @Mapping(source = "email", target = "email", qualifiedByName = EmailVoConverter.TO_EMAIL_VO)
  User toUser(UpdateUserCommand command);

  @Mapping(
      source = "email",
      target = "email",
      qualifiedByName = EmailVoConverter.TO_OPTIONAL_EMAIL_VO)
  User toUser(PatchUserCommand command);
}

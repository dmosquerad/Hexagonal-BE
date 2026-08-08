package com.architecture.hexagonal.infrastructure.inbound.rest.mapper.user;

import com.architecture.hexagonal.domain.model.aggregate.user.User;
import com.architecture.hexagonal.infrastructure.inbound.contract.rest.user.server.dto.UserReadDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.mapstruct.MapstructConfig;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.converter.EmailConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = EmailConverter.class)
public interface UserReadDtoMapper {
  @Mapping(source = "user.userId", target = "userId")
  @Mapping(source = "user.name", target = "name")
  @Mapping(source = "email", target = "email", qualifiedByName = EmailConverter.TO_EMAIL)
  UserReadDto toUserReadDto(User user);
}

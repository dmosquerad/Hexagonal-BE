package com.architecture.hexagonal.infrastructure.inbound.rest.mapper;

import com.architecture.hexagonal.domain.model.entity.User;
import com.architecture.hexagonal.infrastructure.inbound.rest.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.inbound.contract.rest.user.dto.UserReadDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.converter.EmailConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = EmailConverter.class)
public interface UserReadDtoMapper {
  @Mapping(source = "email", target = "email", qualifiedByName = EmailConverter.TO_EMAIL)
  UserReadDto toUserReadDto(User user);
}

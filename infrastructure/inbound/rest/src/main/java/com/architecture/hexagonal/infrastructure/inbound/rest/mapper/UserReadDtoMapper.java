package com.architecture.hexagonal.infrastructure.inbound.rest.mapper;

import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserReadDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserReadDtoMapper {
  UserReadDto toUserReadDto(User user);
}

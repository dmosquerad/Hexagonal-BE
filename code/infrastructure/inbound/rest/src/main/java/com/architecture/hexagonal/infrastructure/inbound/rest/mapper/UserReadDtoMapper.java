package com.architecture.hexagonal.infrastructure.inbound.rest.mapper;

import com.architecture.hexagonal.domain.data.entity.User;
import com.architecture.hexagonal.infrastructure.inbound.rest.dto.UserReadDto;
import com.architecture.hexagonal.infrastructure.inbound.rest.mapper.util.ToEmailUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = ToEmailUtil.class)
public interface UserReadDtoMapper {
  @Mapping(source = "email", target = "email", qualifiedByName = ToEmailUtil.TO_EMAIL)
  UserReadDto toUserReadDto(User user);
}

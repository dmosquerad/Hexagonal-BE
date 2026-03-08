package com.architecture.hexagonal.inbound.rest.mapper;

import com.architecture.hexagonal.domain.data.User;
import com.architecture.hexagonal.inbound.rest.dto.UserReadDto;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface UserReadDtoMapper {

  UserReadDto toUserReadDto(User user);

  Set<UserReadDto> toUserReadDtoSet(Set<User> user);
}

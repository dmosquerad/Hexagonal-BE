package com.architecture.hexagonal.inbound.rest.mapper;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.inbound.rest.dto.UserDto;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface UserDtoMapper {

  UserDto toUserDto(UserDo userDo);

  Set<UserDto> toUserDtoSet(Set<UserDo> userDo);
}

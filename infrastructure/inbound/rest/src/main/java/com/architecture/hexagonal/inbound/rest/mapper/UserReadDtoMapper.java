package com.architecture.hexagonal.inbound.rest.mapper;

import com.architecture.hexagonal.domain.data.UserDo;
import com.architecture.hexagonal.inbound.rest.dto.UserReadDto;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface UserReadDtoMapper {

  UserReadDto toUserReadDto(UserDo userDo);

  Set<UserReadDto> toUserReadDtoSet(Set<UserDo> userDo);
}

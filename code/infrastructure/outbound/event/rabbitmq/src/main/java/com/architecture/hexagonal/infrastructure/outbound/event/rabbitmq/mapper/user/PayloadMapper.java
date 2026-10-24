package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user;

import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserCreated;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserDeleted;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserUpdated;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.config.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface PayloadMapper {

  @Mapping(target = "data", source = "userData")
  PayloadVo toPayload(UserCreated userCreated);

  @Mapping(target = "data", source = "userData")
  PayloadVo toPayload(UserUpdated userUpdated);

  @Mapping(target = "data", source = "userData")
  PayloadVo toPayload(UserDeleted userDeleted);
}

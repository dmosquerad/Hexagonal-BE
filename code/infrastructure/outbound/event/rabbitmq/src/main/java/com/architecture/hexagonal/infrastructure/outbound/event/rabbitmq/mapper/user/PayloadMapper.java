package com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.mapper.user;

import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserCreated;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserDeleted;
import com.architecture.hexagonal.contract.outbound.event.rabbitmq.model.UserUpdated;
import com.architecture.hexagonal.domain.model.vo.payload.PayloadVo;
import com.architecture.hexagonal.infrastructure.outbound.event.rabbitmq.config.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface PayloadMapper {

  PayloadVo toPayload(UserCreated userCreated);

  PayloadVo toPayload(UserUpdated userUpdated);

  PayloadVo toPayload(UserDeleted userDeleted);
}

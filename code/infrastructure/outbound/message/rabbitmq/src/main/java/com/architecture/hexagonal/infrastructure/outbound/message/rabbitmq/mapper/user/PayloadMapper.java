package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.user;

import com.architecture.hexagonal.contract.outbound.rabbitmq.UserCreated;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserDeleted;
import com.architecture.hexagonal.contract.outbound.rabbitmq.UserUpdated;
import com.architecture.hexagonal.domain.model.vo.outbox.PayloadVo;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.config.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface PayloadMapper {

  PayloadVo toPayload(UserCreated userCreated);

  PayloadVo toPayload(UserUpdated userUpdated);

  PayloadVo toPayload(UserDeleted userDeleted);
}

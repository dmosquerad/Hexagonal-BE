package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.mapper.user;

import com.architecture.hexagonal.domain.model.vo.outbox.PayloadVo;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserCreated;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserDeleted;
import com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data.UserUpdated;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface PayloadMapper {

  PayloadVo toPayload(UserCreated userCreated);

  PayloadVo toPayload(UserUpdated userUpdated);

  PayloadVo toPayload(UserDeleted userDeleted);
}

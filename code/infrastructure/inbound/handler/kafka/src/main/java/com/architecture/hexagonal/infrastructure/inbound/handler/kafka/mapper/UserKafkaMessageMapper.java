package com.architecture.hexagonal.infrastructure.inbound.handler.kafka.mapper;

import com.architecture.hexagonal.contract.application.generated.user.CreateUserCommand;
import com.architecture.hexagonal.contract.application.generated.user.DeleteUserCommand;
import com.architecture.hexagonal.contract.application.generated.user.UpdateUserCommand;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserCreatedMessage;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserDeletedMessage;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserUpdatedMessage;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface UserKafkaMessageMapper {

  CreateUserCommand toCreateUserCommand(UserCreatedMessage message);

  UpdateUserCommand toUpdateUserCommand(UserUpdatedMessage message);

  DeleteUserCommand toDeleteUserCommand(UserDeletedMessage message);
}

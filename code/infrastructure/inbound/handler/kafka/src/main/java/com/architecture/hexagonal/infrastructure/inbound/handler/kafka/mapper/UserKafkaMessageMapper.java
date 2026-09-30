package com.architecture.hexagonal.infrastructure.inbound.handler.kafka.mapper;

import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.CreateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.DeleteUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.UpdateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserCreatedMessage;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserDeletedMessage;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserUpdatedMessage;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface UserKafkaMessageMapper {

  CreateUserCommandDto toCreateUserCommand(UserCreatedMessage message);

  UpdateUserCommandDto toUpdateUserCommand(UserUpdatedMessage message);

  DeleteUserCommandDto toDeleteUserCommand(UserDeletedMessage message);
}

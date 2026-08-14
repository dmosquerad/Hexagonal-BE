package com.architecture.hexagonal.infrastructure.inbound.handler.kafka.mapper;

import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.CreateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.DeleteUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.contract.orchestration.generated.user.UpdateUserCommandDto;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserCreatedMessage;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserDeletedMessage;
import com.architecture.hexagonal.infrastructure.inbound.handler.kafka.data.UserUpdatedMessage;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapstructConfig.class)
public interface UserKafkaMessageMapper {

  String TO_UUID = "toUuid";

  CreateUserCommandDto toCreateUserCommand(UserCreatedMessage message);

  @Mapping(source = "userId", target = "userId", qualifiedByName = TO_UUID)
  UpdateUserCommandDto toUpdateUserCommand(UserUpdatedMessage message);

  @Mapping(source = "userId", target = "userId", qualifiedByName = TO_UUID)
  DeleteUserCommandDto toDeleteUserCommand(UserDeletedMessage message);

  @Named(TO_UUID)
  default UUID toUuid(String value) {
    return value == null ? null : UUID.fromString(value);
  }
}

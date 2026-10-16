package com.architecture.hexagonal.infrastructure.inbox.mapper;

import com.architecture.hexagonal.contract.application.usecase.generated.user.CreateUserCommand;
import com.architecture.hexagonal.contract.application.usecase.generated.user.DeleteUserCommand;
import com.architecture.hexagonal.contract.application.usecase.generated.user.UpdateUserCommand;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserCreatedData;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserDeletedData;
import com.architecture.hexagonal.contract.inbound.kafka.model.UserUpdatedData;
import com.architecture.hexagonal.infrastructure.inbox.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface UserCommandFromInboxMapper {

  @Mapping(target = ".", source = "userCreatedData")
  CreateUserCommand toCreateUserCommand(UserCreatedData userCreatedData);

  @Mapping(target = ".", source = "userUpdatedData")
  UpdateUserCommand toUpdateUserCommand(UserUpdatedData userUpdatedData);

  @Mapping(target = ".", source = "userDeletedData")
  DeleteUserCommand toDeleteUserCommand(UserDeletedData userDeletedData);
}

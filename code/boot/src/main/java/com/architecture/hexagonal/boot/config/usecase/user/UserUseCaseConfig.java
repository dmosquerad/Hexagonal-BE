package com.architecture.hexagonal.boot.config.usecase.user;

import com.architecture.hexagonal.application.user.create.usecase.impl.CreateUserUseCaseImpl;
import com.architecture.hexagonal.application.user.delete.usecase.impl.DeleteUserUseCaseImpl;
import com.architecture.hexagonal.application.user.exists.usecase.impl.UserExistsUseCaseImpl;
import com.architecture.hexagonal.application.user.findbyid.usecase.impl.FindUserByUserIdUseCaseImpl;
import com.architecture.hexagonal.application.user.getall.usecase.impl.GetUsersUseCaseImpl;
import com.architecture.hexagonal.application.user.patch.usecase.impl.PatchUserUseCaseImpl;
import com.architecture.hexagonal.application.user.update.usecase.impl.UpdateUserUseCaseImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
  CreateUserUseCaseImpl.class,
  DeleteUserUseCaseImpl.class,
  UserExistsUseCaseImpl.class,
  FindUserByUserIdUseCaseImpl.class,
  GetUsersUseCaseImpl.class,
  PatchUserUseCaseImpl.class,
  UpdateUserUseCaseImpl.class
})
public class UserUseCaseConfig {}

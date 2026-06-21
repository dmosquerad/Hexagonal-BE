package com.architecture.hexagonal.boot.config.usecase.user;

import com.architecture.hexagonal.application.business.user.create.usecase.impl.CreateUserUseCaseImpl;
import com.architecture.hexagonal.application.business.user.delete.usecase.impl.DeleteUserUseCaseImpl;
import com.architecture.hexagonal.application.business.user.exists.usecase.impl.UserExistsUseCaseImpl;
import com.architecture.hexagonal.application.business.user.findbyid.usecase.impl.FindUserByUserIdUseCaseImpl;
import com.architecture.hexagonal.application.business.user.getall.usecase.impl.GetUsersUseCaseImpl;
import com.architecture.hexagonal.application.business.user.patch.usecase.impl.PatchUserUseCaseImpl;
import com.architecture.hexagonal.application.business.user.update.usecase.impl.UpdateUserUseCaseImpl;
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

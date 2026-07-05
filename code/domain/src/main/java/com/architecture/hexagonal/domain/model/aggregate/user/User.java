package com.architecture.hexagonal.domain.model.aggregate.user;

import com.architecture.hexagonal.domain.model.aggregate.Aggregate;
import com.architecture.hexagonal.domain.model.entity.UserDo;
import com.architecture.hexagonal.domain.model.vo.EmailVo;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class User implements Aggregate<UserDo> {

  @Override
  public UUID getId() {
    return user.getUserId();
  }

  @Override
  public UserDo getAggregateRoot() {
    return user;
  }

  UserDo user;
  EmailVo email;
}

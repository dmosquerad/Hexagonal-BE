package com.architecture.hexagonal.domain.model.aggregate;

import com.architecture.hexagonal.domain.model.entity.UserDo;
import com.architecture.hexagonal.domain.model.vo.EmailVo;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class User {

  public UUID getId() {
    return user.getUserId();
  }

  UserDo user;
  EmailVo email;
}

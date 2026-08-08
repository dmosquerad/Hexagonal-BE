package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data;

import com.architecture.hexagonal.domain.model.entity.user.User;
import com.architecture.hexagonal.domain.model.vo.outbox.MessageHeaderVo;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
public class UserUpdated {

  MessageHeaderVo messageHeader;
  User data;
}

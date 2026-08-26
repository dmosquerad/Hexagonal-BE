package com.architecture.hexagonal.infrastructure.outbound.message.rabbitmq.data;

import com.architecture.hexagonal.domain.model.vo.MessageHeaderVo;
import lombok.Data;

@Data
public class UserCreated {

  MessageHeaderVo messageHeader;
  User data;
}

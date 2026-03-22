package com.architecture.hexagonal.domain.data.entity;

import com.architecture.hexagonal.domain.data.vo.EmailVo;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class User {
  UUID userId;
  String name;
  EmailVo email;
}

package com.architecture.hexagonal.domain.model.entity.user;

import com.architecture.hexagonal.domain.model.entity.EntityId;
import com.architecture.hexagonal.domain.model.vo.email.EmailVo;
import java.util.UUID;
import lombok.Builder;

@Builder
public record User(UUID userId, String name, EmailVo email) implements EntityId<UUID> {

  @Override
  public UUID getId() {
    return userId;
  }
}

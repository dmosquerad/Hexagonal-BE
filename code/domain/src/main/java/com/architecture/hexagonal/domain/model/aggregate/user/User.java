package com.architecture.hexagonal.domain.model.aggregate.user;

import com.architecture.hexagonal.domain.model.aggregate.AggregateRoot;
import com.architecture.hexagonal.domain.model.vo.EmailVo;
import java.util.UUID;
import lombok.Builder;

@Builder
public record User(UUID userId, String name, EmailVo email) implements AggregateRoot<UUID> {

  @Override
  public UUID getId() {
    return userId;
  }
}

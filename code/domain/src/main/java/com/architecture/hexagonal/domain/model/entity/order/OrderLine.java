package com.architecture.hexagonal.domain.model.entity.order;

import com.architecture.hexagonal.domain.model.entity.EntityId;
import java.util.UUID;
import lombok.Builder;

@Builder
public record OrderLine(UUID orderLineId) implements EntityId<UUID> {

  @Override
  public UUID getId() {
    return orderLineId;
  }
}

package com.architecture.hexagonal.domain.model.aggregate.order;

import com.architecture.hexagonal.domain.model.aggregate.AggregateRoot;
import com.architecture.hexagonal.domain.model.entity.order.OrderLine;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record Order(UUID orderId, UUID userId, List<OrderLine> orderLines)
    implements AggregateRoot<UUID> {

  @Override
  public UUID getId() {
    return orderId;
  }
}

package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter;

import com.architecture.hexagonal.application.port.out.outbox.OutboxRepositoryReadPort;
import com.architecture.hexagonal.application.projector.outbox.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.OutboxReadMongodbRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OutboxRepositoryReadMongodbAdapterImpl implements OutboxRepositoryReadPort {

  private final OutboxReadMongodbRepository outboxReadMongodbRepository;
  private final OutboxDoFromMongodbMapper outboxDoFromMongodbMapper;

  @Override
  public List<Outbox> findByStatusAndAggregateType(
      final OutboxStatusAndAggregateProjector outboxStatusAndAggregateProjector) {
    return outboxReadMongodbRepository
        .findByStatusAndAggregateTypeOrderByCreatedAtAsc(
            outboxStatusAndAggregateProjector.status(),
            outboxStatusAndAggregateProjector.aggregateType())
        .stream()
        .map(outboxDoFromMongodbMapper::toOutboxEvent)
        .toList();
  }
}

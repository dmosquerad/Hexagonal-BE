package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryReadPort;
import com.architecture.hexagonal.application.usecase.technical.outbox.find.projector.OutboxStatusAndAggregateProjector;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.OutboxReadMongodbRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OutboxRepositoryReadMongodbAdapterImpl implements OutboxRepositoryReadPort {

  private final OutboxReadMongodbRepository outboxReadMongodbRepository;
  private final OutboxDoFromMongodbMapper outboxDoFromMongodbMapper;

  @Override
  public List<Outbox> findByStatusAndAggregateType(
      OutboxStatusAndAggregateProjector outboxStatusAndAggregateProjector) {
    return outboxReadMongodbRepository
        .findByStatusAndAggregateTypeOrderByCreatedAtAsc(
            outboxStatusAndAggregateProjector.status(),
            outboxStatusAndAggregateProjector.aggregateType())
        .stream()
        .map(outboxDoFromMongodbMapper::toOutboxEvent)
        .collect(Collectors.toList());
  }
}

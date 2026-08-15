package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryReadPort;
import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import com.architecture.hexagonal.domain.model.vo.OutboxStatusVo;
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
  public List<Outbox> findPendingEvents() {
    return outboxReadMongodbRepository
        .findByStatusOrderByCreatedAtAsc(OutboxStatusVo.PENDING)
        .stream()
        .map(outboxDoFromMongodbMapper::toOutboxEvent)
        .collect(Collectors.toList());
  }
}

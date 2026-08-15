package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryWritePort;
import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.OutboxWriteMongodbRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OutboxRepositoryWriteMongodbAdapterImpl implements OutboxRepositoryWritePort {

  private final OutboxWriteMongodbRepository outboxWriteMongodbRepository;
  private final OutboxDoFromMongodbMapper outboxDoFromMongodbMapper;
  private final OutboxDaoMapper outboxDaoMapper;

  @Override
  public Outbox save(@NonNull Outbox outbox) {
    return outboxDoFromMongodbMapper.toOutboxEvent(
        outboxWriteMongodbRepository.save(outboxDaoMapper.toOutboxEventDao(outbox)));
  }
}

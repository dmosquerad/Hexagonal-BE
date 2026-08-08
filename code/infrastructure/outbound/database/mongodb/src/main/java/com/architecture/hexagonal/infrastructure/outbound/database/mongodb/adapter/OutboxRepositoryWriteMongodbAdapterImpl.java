package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryWritePort;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.OutboxWriteMongodbRepository;
import java.util.Optional;
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
  public Outbox upsertByEventIdentity(@NonNull Outbox outbox) {
    return outboxDoFromMongodbMapper.toOutboxEvent(
        outboxWriteMongodbRepository.upsertByEventIdentity(
            outboxDaoMapper.toOutboxEventDao(outbox)));
  }

  @Override
  public Optional<Outbox> claimPendingForProcessing(@NonNull Outbox outbox) {
    return Optional.ofNullable(
            outboxWriteMongodbRepository.claimPendingForProcessing(
                outboxDaoMapper.toOutboxEventDao(outbox)))
        .map(outboxDoFromMongodbMapper::toOutboxEvent);
  }
}

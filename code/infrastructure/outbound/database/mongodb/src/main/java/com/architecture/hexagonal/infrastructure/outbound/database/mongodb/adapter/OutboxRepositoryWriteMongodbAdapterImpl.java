package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter;

import com.architecture.hexagonal.application.port.database.OutboxRepositoryWritePort;
import com.architecture.hexagonal.domain.model.aggregate.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDaoMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox.OutboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.OutboxReadMongodbRepository;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.OutboxWriteMongodbRepository;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OutboxRepositoryWriteMongodbAdapterImpl implements OutboxRepositoryWritePort {

  private final OutboxReadMongodbRepository outboxReadMongodbRepository;
  private final OutboxWriteMongodbRepository outboxWriteMongodbRepository;
  private final OutboxDoFromMongodbMapper outboxDoFromMongodbMapper;
  private final OutboxDaoMapper outboxDaoMapper;

  @Override
  public Outbox save(@NonNull Outbox outbox) {
    final Optional<OutboxDao> currentOutbox =
        outboxReadMongodbRepository.findByAggregateTypeAndAggregateIdAndActionAndPayload(
            outbox.aggregateType(), outbox.aggregateId(), outbox.action(), outbox.payload());

    if (currentOutbox.isPresent()) {
      outbox =
          outbox.toBuilder()
              .outboxId(currentOutbox.get().getOutboxId())
              .retryCount(
                  outbox.retryCount() == 0
                      ? currentOutbox.get().getRetryCount() + 1
                      : outbox.retryCount())
              .build();
    }

    return outboxDoFromMongodbMapper.toOutboxEvent(
        outboxWriteMongodbRepository.save(outboxDaoMapper.toOutboxEventDao(outbox)));
  }
}

package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.adapter.inbox;

import com.architecture.hexagonal.application.port.out.inbox.InboxRepositoryReadPort;
import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.inbox.InboxDoFromMongodbMapper;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox.InboxReadMongodbRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InboxRepositoryReadMongodbAdapterImpl implements InboxRepositoryReadPort {

  private final InboxReadMongodbRepository inboxReadMongodbRepository;
  private final InboxDoFromMongodbMapper inboxDoFromMongodbMapper;

  @Override
  public List<Inbox> findByStatusAndAggregateType(
      final StatusVo status, final AggregateTypeVo aggregateType) {
    return inboxReadMongodbRepository
        .findByStatusAndAggregateTypeOrderByCreatedAtAsc(status, aggregateType)
        .stream()
        .map(inboxDoFromMongodbMapper::toInbox)
        .toList();
  }
}

package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.inbox;

import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;
import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InboxReadMongodbRepository extends MongoRepository<InboxDao, UUID> {
  List<InboxDao> findByStatusAndAggregateTypeOrderByCreatedAtAsc(
      StatusVo status, AggregateTypeVo aggregateType);
}

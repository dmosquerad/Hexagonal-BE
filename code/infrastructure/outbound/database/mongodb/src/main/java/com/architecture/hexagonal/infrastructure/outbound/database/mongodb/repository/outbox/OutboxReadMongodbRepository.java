package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository.outbox;

import com.architecture.hexagonal.domain.model.vo.payload.AggregateTypeVo;
import com.architecture.hexagonal.domain.model.vo.payload.StatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.outbox.OutboxDao;
import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OutboxReadMongodbRepository extends MongoRepository<OutboxDao, UUID> {

  List<OutboxDao> findByStatusAndAggregateTypeOrderByCreatedAtAsc(
      StatusVo status, AggregateTypeVo aggregateType);
}

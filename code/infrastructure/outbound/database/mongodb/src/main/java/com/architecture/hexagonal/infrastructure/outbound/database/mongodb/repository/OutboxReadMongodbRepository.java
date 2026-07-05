package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.repository;

import com.architecture.hexagonal.domain.model.vo.OutboxStatusVo;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.OutboxDao;
import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OutboxReadMongodbRepository extends MongoRepository<OutboxDao, UUID> {

  List<OutboxDao> findByStatusOrderByCreatedAtAsc(OutboxStatusVo status);
}

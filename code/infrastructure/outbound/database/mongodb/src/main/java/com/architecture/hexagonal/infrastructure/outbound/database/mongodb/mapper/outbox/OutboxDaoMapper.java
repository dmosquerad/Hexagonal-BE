package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox;

import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.OutboxDao;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface OutboxDaoMapper {

  OutboxDao toOutboxEventDao(Outbox outbox);
}

package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox;

import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.outbox.OutboxDao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public interface OutboxDaoMapper {

  @Mapping(source = "createdAt", target = "createdAt")
  @Mapping(source = "processedAt", target = "processedAt")
  @Mapping(
      source = "payload.messageHeader.messageDate",
      target = "payload.messageHeader.messageDate")
  OutboxDao toOutboxDao(Outbox outbox);
}

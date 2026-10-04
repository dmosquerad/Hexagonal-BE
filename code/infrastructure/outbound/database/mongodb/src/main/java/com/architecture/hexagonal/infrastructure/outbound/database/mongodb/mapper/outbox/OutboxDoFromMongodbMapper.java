package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox;

import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.converter.OffsetDateTimeConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = OffsetDateTimeConverter.class)
public interface OutboxDoFromMongodbMapper {

  @Mapping(
      source = "createdAt",
      target = "createdAt",
      qualifiedByName = OffsetDateTimeConverter.TO_OFFSET_DATE_TIME)
  @Mapping(
      source = "processedAt",
      target = "processedAt",
      qualifiedByName = OffsetDateTimeConverter.TO_OFFSET_DATE_TIME)
  @Mapping(
      source = "payload.messageHeader.messageDate",
      target = "payload.messageHeader.messageDate",
      qualifiedByName = OffsetDateTimeConverter.TO_OFFSET_DATE_TIME)
  Outbox toOutboxEvent(OutboxDao outboxDao);
}

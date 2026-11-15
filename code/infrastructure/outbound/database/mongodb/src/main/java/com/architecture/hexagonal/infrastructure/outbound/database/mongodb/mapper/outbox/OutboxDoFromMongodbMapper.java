package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.outbox;

import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.outbox.OutboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.converter.DateVoMongoConverter;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.payload.MessageHeaderVoFromMongodbMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = DateVoMongoConverter.class)
public interface OutboxDoFromMongodbMapper extends MessageHeaderVoFromMongodbMapper {

  @Mapping(
      source = "createdAt",
      target = "createdAt",
      qualifiedByName = DateVoMongoConverter.TO_DATE_VO)
  @Mapping(
      source = "processedAt",
      target = "processedAt",
      qualifiedByName = DateVoMongoConverter.TO_DATE_VO)
  Outbox toOutbox(OutboxDao outboxDao);
}

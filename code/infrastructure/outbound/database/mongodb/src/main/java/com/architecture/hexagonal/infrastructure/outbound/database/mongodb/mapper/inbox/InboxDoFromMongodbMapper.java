package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.inbox;

import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.converter.DateVoMongoConverter;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.payload.MessageHeaderVoFromMongodbMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = DateVoMongoConverter.class)
public interface InboxDoFromMongodbMapper extends MessageHeaderVoFromMongodbMapper {

  @Mapping(source = "aggregateId", target = "aggregateId")
  @Mapping(
      source = "createdAt",
      target = "createdAt",
      qualifiedByName = DateVoMongoConverter.TO_DATE_VO)
  @Mapping(
      source = "processedAt",
      target = "processedAt",
      qualifiedByName = DateVoMongoConverter.TO_DATE_VO)
  Inbox toInbox(InboxDao inboxDao);
}

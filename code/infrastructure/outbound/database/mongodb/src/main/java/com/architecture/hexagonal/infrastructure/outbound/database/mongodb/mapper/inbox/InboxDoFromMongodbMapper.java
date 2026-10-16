package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.inbox;

import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.converter.OffsetDateTimeConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = OffsetDateTimeConverter.class)
public interface InboxDoFromMongodbMapper {

  @Mapping(source = "aggregateId", target = "aggregateId")
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
  Inbox toInbox(InboxDao inboxDao);
}

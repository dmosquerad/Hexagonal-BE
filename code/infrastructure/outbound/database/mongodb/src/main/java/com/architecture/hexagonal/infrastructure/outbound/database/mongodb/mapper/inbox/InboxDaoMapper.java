package com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.inbox;

import com.architecture.hexagonal.domain.model.entity.inbox.Inbox;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.config.MapstructConfig;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.data.inbox.InboxDao;
import com.architecture.hexagonal.infrastructure.outbound.database.mongodb.mapper.converter.DateVoMongoConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class, uses = DateVoMongoConverter.class)
public interface InboxDaoMapper {

  @Mapping(target = "inboxId", ignore = true)
  @Mapping(
      source = "createdAt",
      target = "createdAt",
      qualifiedByName = DateVoMongoConverter.TO_DATE_VO_DAO)
  @Mapping(
      source = "processedAt",
      target = "processedAt",
      qualifiedByName = DateVoMongoConverter.TO_DATE_VO_DAO)
  @Mapping(
      source = "payload.messageHeader.messageDate",
      target = "payload.messageHeader.messageDate",
      qualifiedByName = DateVoMongoConverter.TO_DATE_VO_DAO)
  InboxDao toInboxDao(Inbox inbox);
}

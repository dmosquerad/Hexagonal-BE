package com.architecture.hexagonal.application.mapper.date;

import com.architecture.hexagonal.application.mapper.converter.ZoneIdConverter;
import com.architecture.hexagonal.contract.bus.model.common.Date;
import com.architecture.hexagonal.domain.model.vo.time.DateVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ZoneIdConverter.class)
public interface DateVoMapper {
  DateVoMapper INSTANCE = Mappers.getMapper(DateVoMapper.class);

  @Mapping(source = "zone", target = "zone", qualifiedByName = ZoneIdConverter.TO_ZONE_ID)
  DateVo toDateVo(Date date);
}

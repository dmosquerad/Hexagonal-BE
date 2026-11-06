package com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.mapper.email;

import com.architecture.hexagonal.contract.inbound.endpoint.rest.email.server.dto.EmailBlockRulesDto;
import com.architecture.hexagonal.domain.model.vo.email.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.inbound.endpoint.rest.config.mapstruct.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public interface EmailBlockRulesDtoMapper {

  EmailBlockRulesDto toEmailBlockRulesDto(EmailBlockRulesVo source);
}

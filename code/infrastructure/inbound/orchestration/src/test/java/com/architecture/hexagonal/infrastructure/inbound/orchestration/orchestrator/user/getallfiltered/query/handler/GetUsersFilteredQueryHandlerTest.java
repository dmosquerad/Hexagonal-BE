package com.architecture.hexagonal.infrastructure.inbound.orchestration.orchestrator.user.getallfiltered.query.handler;

import com.architecture.hexagonal.application.business.email.getblockedrules.usecase.GetBlockedRulesUseCase;
import com.architecture.hexagonal.application.business.user.getall.input.GetUsersQuery;
import com.architecture.hexagonal.application.business.user.getall.usecase.GetAllUsersUseCase;
import com.architecture.hexagonal.domain.exception.DomainException;
import com.architecture.hexagonal.domain.model.aggregate.User;
import com.architecture.hexagonal.domain.model.pagination.PaginationResult;
import com.architecture.hexagonal.domain.model.vo.EmailBlockRulesVo;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.common.PaginationDto;
import com.architecture.hexagonal.infrastructure.contract.orchestration.generated.user.GetUsersFilteredQueryDto;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.mapper.pagination.PaginationMapper;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.common.PaginationDtoTestDataBuilder;
import com.architecture.hexagonal.infrastructure.inbound.orchestration.testutils.data.query.GetUsersFilteredQueryDtoTestDataBuilder;
import java.util.Collections;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUsersFilteredQueryHandlerTest {

  @InjectMocks private GetUsersFilteredQueryHandler queryHandler;

  @Mock private GetAllUsersUseCase getAllUsersUseCase;

  @Mock private GetBlockedRulesUseCase getBlockedRulesUseCase;

  @Spy private PaginationMapper paginationMapper = Mappers.getMapper(PaginationMapper.class);

  @Test
  void handle_shouldReturnPaginationResult_whenBlockEmailIsNull() throws DomainException {
    final PaginationResult<User> expectedResult =
        PaginationResult.<User>builder()
            .data(Collections.emptyList())
            .page(0)
            .size(10)
            .totalElements(0L)
            .totalPages(0)
            .build();

    GetUsersFilteredQueryDto queryDto =
        GetUsersFilteredQueryDtoTestDataBuilder.builder()
            .host("example")
            .blockEmail(null)
            .pagination(null)
            .build()
            .getUsersFilteredQueryDto();
    Mockito.when(getAllUsersUseCase.execute(ArgumentMatchers.any(GetUsersQuery.class)))
        .thenReturn(expectedResult);

    PaginationResult<User> result = queryHandler.handle(queryDto);

    AssertionsForClassTypes.assertThat(result).isSameAs(expectedResult);

    Mockito.verify(paginationMapper).toPagination((PaginationDto) ArgumentMatchers.isNull());
    Mockito.verify(getAllUsersUseCase).execute(ArgumentMatchers.any(GetUsersQuery.class));
    Mockito.verify(getBlockedRulesUseCase, Mockito.never()).execute();
  }

  @Test
  void handle_shouldReturnPaginationResult_whenBlockEmailIsProvided() throws DomainException {
    final EmailBlockRulesVo blockedRules = EmailBlockRulesVo.builder().build();
    final PaginationResult<User> expectedResult =
        PaginationResult.<User>builder()
            .data(Collections.emptyList())
            .page(1)
            .size(20)
            .totalElements(0L)
            .totalPages(0)
            .build();

    GetUsersFilteredQueryDto queryDto =
        GetUsersFilteredQueryDtoTestDataBuilder.builder()
            .host("example")
            .blockEmail(true)
            .pagination(
                PaginationDtoTestDataBuilder.builder().page(1).size(20).build().paginationDto())
            .build()
            .getUsersFilteredQueryDto();
    Mockito.when(getBlockedRulesUseCase.execute()).thenReturn(blockedRules);
    Mockito.when(getAllUsersUseCase.execute(ArgumentMatchers.any(GetUsersQuery.class)))
        .thenReturn(expectedResult);

    PaginationResult<User> result = queryHandler.handle(queryDto);

    AssertionsForClassTypes.assertThat(result).isSameAs(expectedResult);

    Mockito.verify(paginationMapper).toPagination(ArgumentMatchers.any(PaginationDto.class));
    Mockito.verify(getBlockedRulesUseCase).execute();
    Mockito.verify(getAllUsersUseCase).execute(ArgumentMatchers.any(GetUsersQuery.class));
  }
}
